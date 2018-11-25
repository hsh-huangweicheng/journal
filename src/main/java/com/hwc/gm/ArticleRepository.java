package com.hwc.gm;

import com.hwc.gm.bean.Article;
import org.springframework.data.repository.CrudRepository;

public interface ArticleRepository extends CrudRepository<Article, Integer> {
}
