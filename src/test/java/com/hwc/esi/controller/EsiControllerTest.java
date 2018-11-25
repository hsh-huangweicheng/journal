package com.hwc.esi.controller;

import com.hwc.gm.bean.Article;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class EsiControllerTest {

    private EsiController esiController;

    @Before
    public void before() {
        esiController = new EsiController();
    }

    @Test
    public void parseEsiArticle() throws IOException {
        File file = new File("C:/Users/Administrator/Desktop/China-high/2materials.xlsx");
        Workbook workbook = esiController.readExcel(file);
        List<Article> esiArticles = esiController.parseEsiArticle(workbook);
        System.out.println(esiArticles);
    }
}