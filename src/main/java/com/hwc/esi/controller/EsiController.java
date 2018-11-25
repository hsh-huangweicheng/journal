package com.hwc.esi.controller;

import com.google.common.collect.Lists;
import com.hwc.common.Utils;
import com.hwc.gm.ArticleRepository;
import com.hwc.gm.bean.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping(path = "/esi")
public class EsiController {

    private static final String EXCEL_XLS = "xls";
    private static final String EXCEL_XLSX = "xlsx";

    @Autowired
    private ArticleRepository articleRepository;

    @PersistenceContext
    private EntityManager entityManager;


    Map<Integer, TripleConsume<String, Article, Long>> cellMapping = new HashMap<>();

    {
        cellMapping.put(3, (value, article, id) -> article.setArticleName(value));
        cellMapping.put(4, (value, article, id) -> {
            AtomicInteger count = new AtomicInteger();
            List<Author> list = Arrays.asList(value.split(";")).stream().distinct()
                    .map(name -> new Author(id, name, count.getAndIncrement())).collect(Collectors.toList());
            article.setAuthorList(list);
        });
        cellMapping.put(5, (value, article, id) -> article.setSource(value));
        cellMapping.put(6, (value, article, id) -> article.setResearchField(value));
        cellMapping.put(7, (value, article, id) -> article.setCited(Integer.parseInt(value)));
        cellMapping.put(8, (value, article, id) -> {
            AtomicInteger count = new AtomicInteger();
            List<Country> list = Arrays.asList(value.split(";")).stream().distinct()
                    .map(name -> new Country(id, name, count.getAndIncrement())).collect(Collectors.toList());

            article.setCountryList(list);
        });
        cellMapping.put(9, (value, article, id) -> {
            AtomicInteger count = new AtomicInteger();
            List<Address> list = Arrays.asList(value.split(";")).stream().distinct()
                    .map(name -> new Address(id, name, count.getAndIncrement())).collect(Collectors.toList());

            article.setAddressList(list);
        });

        cellMapping.put(10, (value, article, id) -> {
            AtomicInteger count = new AtomicInteger();
            List<Institution> list = Arrays.asList(value.split(";")).stream().distinct()
                    .map(name -> new Institution(id, name, count.getAndIncrement())).collect(Collectors.toList());

            article.setInstitutionList(list);
        });
        cellMapping.put(11, (value, article, id) -> article.setYear(Integer.parseInt(value)));
    }

    @GetMapping(path = "/import")
    public @ResponseBody
    String addNewUser() throws IOException {

        String path = "C:\\Users\\Administrator\\Desktop\\China-high";
        List<Article> esiArticleList = parseEsiArticles(Paths.get(path));

        Lists.partition(esiArticleList, 300).parallelStream().forEach(articleRepository::saveAll);

        return String.format("导入了{%s}条数据", esiArticleList.size());
    }

    private List<Article> parseEsiArticles(Path path) throws IOException {
        List<Path> allPathInDirectory = Utils.getAllPathInDirectory(path, EXCEL_XLS, EXCEL_XLSX);

        return allPathInDirectory.stream().parallel().flatMap(excelFilePath -> {
            Workbook workbook = readExcel(excelFilePath.toFile());
            return parseEsiArticle(workbook).stream();
        }).collect(Collectors.toList());
    }

    public List<Article> parseEsiArticle(Workbook workbook) {
        Sheet sheet = workbook.getSheetAt(0);
        List<Article> esiArticleList = IntStream.range(sheet.getFirstRowNum(), sheet.getLastRowNum()).mapToObj(index -> {
            Row row = sheet.getRow(index);
            String accessionNumber = row.getCell(0).getStringCellValue();

            if (accessionNumber.startsWith("WOS:")) {
                Article article = new Article();
                long id = NumberUtils.toLong(accessionNumber.replace("WOS:", "").replaceAll("^0+", ""));
                article.setId(id);
                cellMapping.entrySet().forEach(entry -> {
                    Cell cell = row.getCell(entry.getKey());
                    entry.getValue().accept(cell.toString(), article, id);
                });

                return article;
            }
            return null;
        }).filter(Objects::nonNull).collect(Collectors.toList());

        return esiArticleList;
    }

    public Workbook readExcel(File file) {
        try (InputStream in = new FileInputStream(file)) {
            if (file.getName().endsWith(EXCEL_XLS)) {
                return new HSSFWorkbook(in);
            } else {
                return new XSSFWorkbook(in);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private interface TripleConsume<T, U, P> {
        void accept(T t, U u, P p);
    }
}
