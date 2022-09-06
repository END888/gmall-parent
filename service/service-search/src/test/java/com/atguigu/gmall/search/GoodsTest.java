package com.atguigu.gmall.search;

import com.atguigu.gmall.model.list.Goods;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.NestedQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.HighlightQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;

import java.util.List;

@SpringBootTest
public class GoodsTest {

    @Autowired
    ElasticsearchRestTemplate esTemplate;

    /**
     * 查询一级分类id为2，二级分类id为13、三级分类id为61的商品
     */
    @Test
    void test01(){
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        boolQuery.must(QueryBuilders.termQuery("category1Id",2));
        boolQuery.must(QueryBuilders.termQuery("category2Id",13));
        boolQuery.must(QueryBuilders.termQuery("category3Id",61));

        SearchHits<Goods> searchHits = esTemplate
                .search(new NativeSearchQuery(boolQuery), Goods.class, IndexCoordinates.of("goods"));
        List<SearchHit<Goods>> hitList = searchHits.getSearchHits();
        for (SearchHit<Goods> hit : hitList) {
            Goods content = hit.getContent();
            System.out.println("content = " + content);
        }
        System.out.println("总数：" +searchHits.getTotalHits() );
    }

    /**
     * 查询一级分类id为2，二级分类id为13、三级分类id为61,并且价格大于4000的商品
     */
    @Test
    void test02(){
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        boolQuery.must(QueryBuilders.termQuery("category1Id",2));
        boolQuery.must(QueryBuilders.termQuery("category2Id",13));
        boolQuery.must(QueryBuilders.termQuery("category3Id",61));
        boolQuery.must(QueryBuilders.rangeQuery("price").gt(4000));

        SearchHits<Goods> searchHits = esTemplate
                .search(new NativeSearchQuery(boolQuery), Goods.class, IndexCoordinates.of("goods"));
        List<SearchHit<Goods>> hitList = searchHits.getSearchHits();
        for (SearchHit<Goods> hit : hitList) {
            Goods content = hit.getContent();
            System.out.println("content = " + content);
        }
        System.out.println("总数：" +searchHits.getTotalHits() );
    }

    /**
     * 查询一级分类id为2，二级分类id为13、三级分类id为61,并且价格大于4000，并且它的平台属性中 attrId=4，attrValue=128GB 的商品
     */
    @Test
    void test03(){
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        boolQuery.must(QueryBuilders.termQuery("category1Id",2));
        boolQuery.must(QueryBuilders.termQuery("category2Id",13));
        boolQuery.must(QueryBuilders.termQuery("category3Id",61));
        boolQuery.must(QueryBuilders.rangeQuery("price").gt(4000));

        BoolQueryBuilder nestedBoolQuery = QueryBuilders.boolQuery();
        nestedBoolQuery.must(QueryBuilders.termQuery("attrs.attrId",4));
        nestedBoolQuery.must(QueryBuilders.termQuery("attrs.attrValue","128GB"));

        NestedQueryBuilder nestedQuery = QueryBuilders.nestedQuery("attrs", nestedBoolQuery, ScoreMode.None);
        boolQuery.must(nestedQuery);

        SearchHits<Goods> searchHits = esTemplate
                .search(new NativeSearchQuery(boolQuery), Goods.class, IndexCoordinates.of("goods"));
        List<SearchHit<Goods>> hitList = searchHits.getSearchHits();
        for (SearchHit<Goods> hit : hitList) {
            Goods content = hit.getContent();
            System.out.println("content = " + content);
        }
        System.out.println("总数：" +searchHits.getTotalHits() );
    }

    /**
     * 查询一级分类id为2，二级分类id为13、三级分类id为61,并且价格大于4000，并且它的平台属性中 attrId=4，attrValue=128GB 并且 attrId=5,attrValue=麒麟990
     */
    @Test
    void test04(){
        // 构建最外层的 boolQuery
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        boolQuery.must(QueryBuilders.matchQuery("title","华为"));
        boolQuery.must(QueryBuilders.termQuery("category1Id",2));
        boolQuery.must(QueryBuilders.termQuery("category2Id",13));
        boolQuery.must(QueryBuilders.termQuery("category3Id",61));
        boolQuery.must(QueryBuilders.rangeQuery("price").gt(4000));

        // 构建中间层的 boolQuery
//        BoolQueryBuilder boolQuery2 = QueryBuilders.boolQuery();
        // ------------------------------------------------------------
        // 构建第一个嵌套查询
        BoolQueryBuilder nestedBoolQuery1 = QueryBuilders.boolQuery();
        nestedBoolQuery1.must(QueryBuilders.termQuery("attrs.attrId",4));
        nestedBoolQuery1.must(QueryBuilders.termQuery("attrs.attrValue","128GB"));
        NestedQueryBuilder nestedQuery1 = QueryBuilders.nestedQuery("attrs",nestedBoolQuery1,ScoreMode.None);


        // 构建第二个嵌套查询
        BoolQueryBuilder nestedBoolQuery2 = QueryBuilders.boolQuery();
        nestedBoolQuery2.must(QueryBuilders.termQuery("attrs.attrId",5));
        nestedBoolQuery2.must(QueryBuilders.termQuery("attrs.attrValue","麒麟990"));
        NestedQueryBuilder nestedQuery2 = QueryBuilders.nestedQuery("attrs",nestedBoolQuery2,ScoreMode.None);
        // ------------------------------------------------------------

        // 将两个嵌套查询构建到中间层的 boolQuery 中
//        boolQuery2.must(nestedQuery1);
//        boolQuery2.must(nestedQuery2);
        // 将中间层的 boolQuery 构建到最外层的 boolQuery 中
//        boolQuery.must(boolQuery2);
        // TODO ：可以直接将 nested 的 boolQuery 构建到外面的 boolQuery
        boolQuery.must(nestedQuery1);
        boolQuery.must(nestedQuery2);

        // 构建 query
        NativeSearchQuery query = new NativeSearchQuery(boolQuery);
        // 按价格升序
        query = query.addSort(Sort.by("price").ascending());
        // 设置分页
        query = query.setPageable(PageRequest.of(0,1));
        // 设置高亮
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("title").preTags("<span style='color:red'>").postTags("</span>");
        query.setHighlightQuery(new HighlightQuery(highlightBuilder));
        // 执行 DSL
        SearchHits<Goods> searchHits = esTemplate
                .search( query,Goods.class, IndexCoordinates.of("goods"));
        List<SearchHit<Goods>> hitList = searchHits.getSearchHits();
        for (SearchHit<Goods> hit : hitList) {
            Goods content = hit.getContent();
            System.out.println("content = " + content);
        }
        System.out.println("总数：" +searchHits.getTotalHits() );
    }
}
