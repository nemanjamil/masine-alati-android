package rs.tridanwebshop.tridan.common.utils;


import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import rs.tridanwebshop.tridan.models.articles.Article;

public class SortUtils {

    public static List<Article> sortArticlesByPriceAscending(List<Article> articles) {
        Collections.sort(articles, new Comparator<Article>() {
            @Override
            public int compare(Article a1, Article a2) {
                if (Conversions.priceStringToFloat(a1.getCenaSamoBrojFormat()) == Conversions.priceStringToFloat(a2.getCenaSamoBrojFormat()))
                    return 0;
                if (Conversions.priceStringToFloat(a1.getCenaSamoBrojFormat()) > Conversions.priceStringToFloat(a2.getCenaSamoBrojFormat()))
                    return 1;
                return -1;
            }
        });
        return articles;
    }

    public static List<Article> sortArticlesByPriceDescending(List<Article> articles) {
        Collections.sort(articles, new Comparator<Article>() {
            @Override
            public int compare(Article a1, Article a2) {
                if (Conversions.priceStringToFloat(a1.getCenaSamoBrojFormat()) == Conversions.priceStringToFloat(a2.getCenaSamoBrojFormat()))
                    return 0;
                if (Conversions.priceStringToFloat(a1.getCenaSamoBrojFormat()) > Conversions.priceStringToFloat(a2.getCenaSamoBrojFormat()))
                    return -1;
                return 1;
            }
        });
        return articles;
    }

    public static List<Article> sortArticlesByNameAscending(List<Article> articles) {
        Collections.sort(articles, new Comparator<Article>() {
            @Override
            public int compare(Article a1, Article a2) {

                return a1.getArtikalNaziv().compareToIgnoreCase(a2.getArtikalNaziv());

            }

        });
        return articles;
    }

    public static List<Article> sortArticlesByNumberOfViewsDescending(List<Article> articles) {
        Collections.sort(articles, new Comparator<Article>() {
            @Override
            public int compare(Article a1, Article a2) {
                if (a1.getArtikalBrPregleda().equals(a2.getArtikalBrPregleda()))
                    return 0;
                if (a1.getArtikalBrPregleda() > a2.getArtikalBrPregleda())
                    return -1;
                return 1;
            }
        });
        return articles;
    }

    public static List<Article> sortArticlesByIdDescending(List<Article> articles) {
        Collections.sort(articles, new Comparator<Article>() {
            @Override
            public int compare(Article a1, Article a2) {
                if (a1.getArtikalId().equals(a2.getArtikalId()))
                    return 0;
                if (a1.getArtikalId() > a2.getArtikalId())
                    return -1;
                return 1;
            }

        });
        return articles;
    }
}
