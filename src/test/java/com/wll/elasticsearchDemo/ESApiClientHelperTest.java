package com.wll.elasticsearchDemo;

import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.ListenableActionFuture;
import org.elasticsearch.action.get.GetRequestBuilder;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryAction;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ESApiClientHelperTest {
    @Autowired
    private ESApiClientHelper helper;

    private final static String article = "article";
    private final static String content = "content";

    @Test
    public void estest() {

        GetResponse getFields = helper.getClient().prepareGet("accounts", "person", "1").get();
        Map<String, Object> source = getFields.getSource();
        source.forEach((k, v) -> {
            System.out.println(k + " : " + v);
        });

    }


    @Test
    public void addIndexAndDocument() throws Exception {
        Date time = new Date();
        IndexResponse response = helper.getClient().prepareIndex(article, content).setSource(XContentFactory.jsonBuilder()
                .startObject().field("id", "447")
                .field("author", "wanglili")
                .field("title", "192.138.1.2")
                .field("content", "这是JAVA有关的书籍")
                .field("price", "20")
                .field("view", "100")
                .field("tag", "a,b,c,d,e,f")
                .field("date", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(time)).endObject()).get();
        System.out.println("添加索引成功,版本号：" + response.getVersion());
    }

    /**
     * 根据index、type、id进行查询
     */
    @Test
    public void searchByIndex(){

        GetResponse response = helper.getClient().prepareGet(article,content,"AWZWuqUic8nQ5f5otOpO").execute()
                .actionGet();
        String json = response.getSourceAsString();
        if (null != json) {
            System.out.println(json);
        } else {
            System.out.println("未查询到任何结果！");
        }
    }


    /**
     * 使用get查询
     */
    @Test
    public void testGet() {
        GetRequestBuilder requestBuilder = helper.getClient().prepareGet("article", "content", "AWZWuqUic8nQ5f5otOpO");
        GetResponse response = requestBuilder.execute().actionGet();
        GetResponse getResponse = requestBuilder.get();
        ListenableActionFuture<GetResponse> execute = requestBuilder.execute();
        System.out.println(response.getSourceAsString());
    }



    /**
     * 更新文档
     * @throws Exception
     */
    @Test
    public void updateDocument() throws Exception{

        Date time = new Date();

        //创建修改请求
        UpdateRequest updateRequest = new UpdateRequest();
        updateRequest.index(article);
        updateRequest.type(content);
        updateRequest.id("AWZWuqUic8nQ5f5otOpO");
        updateRequest.doc(XContentFactory.jsonBuilder()
                .startObject()
                .field("title","192.168.164.129")
                .field("date",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(time))
                .endObject());

        UpdateResponse response = helper.getClient().update(updateRequest).get();
        System.out.println("更新索引成功");
    }

    /**
     * 查询article索引下的所有数据
     * @throws Exception
     */
    @Test
    public  void matchAllQuery() throws Exception{
        QueryBuilder queryBuilder = QueryBuilders.matchAllQuery();
        SearchResponse response = helper.getClient().prepareSearch(article).setQuery(queryBuilder).get();
        for (SearchHit searchHit : response.getHits()) {
//            Map<String, Object> map = searchHit.getSource();
            println(searchHit);
        }
    }






    /**
     * 输出结果SearchResponse
     * @param response
     */
    public static void println(SearchResponse response){
        System.err.println("*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-");
        System.err.println(
                "getFailedShards : " + response.getFailedShards() + "\n" +
                        "getNumReducePhases : " + response.getNumReducePhases() + "\n" +
                        "getScrollId : " + response.getScrollId() +  "\n" +
                        "getTookInMillis : " + response.getTookInMillis() + "\n" +
                        "getTotalShards : " + response.getTotalShards() +  "\n" +
                        "getAggregations : " + response.getAggregations() + "\n" +
                        "getProfileResults : " + response.getProfileResults() + "\n" +
                        "getShardFailures : " + response.getShardFailures() + "\n" +
                        "getSuggest : " + response.getSuggest() + "\n" +
                        "getTook : " + response.getTook() + "\n" +
                        "isTerminatedEarly : " + response.isTerminatedEarly() + "\n" +
                        "isTimedOut : " + response.isTimedOut() + "\n" +
                        "remoteAddress : " + response.remoteAddress() + "\n" +
                        "status : " + response.status() + "\n" +
                        "getHits : " + response.getHits()
        );
    }

    /**
     * 输出结果SearchResponse
     */
    public static void println(SearchHit searchHit){
        System.err.println("*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-");
        System.err.println(
                "docId : " + searchHit.docId() + "\n" +
                        "getId : " + searchHit.getId() + "\n" +
                        "getIndex : " + searchHit.getIndex()+ "\n" +
                        "getScore : " + searchHit.getScore() + "\n" +
                        "getSourceAsString : " + searchHit.getSourceAsString() + "\n" +
                        "getType : " + searchHit.getType() + "\n" +
                        "getVersion : " + searchHit.getVersion() + "\n" +
                        "fieldsOrNull : " + searchHit.fieldsOrNull() + "\n" +
                        "getExplanation : " + searchHit.getExplanation() + "\n" +
                        "getFields : " + searchHit.getFields() + "\n" +
                        "highlightFields : " + searchHit.highlightFields() + "\n" +
                        "hasSource : " + searchHit.hasSource()
        );
    }


//*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-自己网上找的测试*-*-*-*-*-*-*-*-*-*-*-*-*-*-*
    @Test
    public void getColor(){
        SearchResponse response = helper.getClient().prepareSearch("cars")
                .setTypes("transactions")
                .addAggregation(AggregationBuilders.terms("popular_colors")
                        .field("color.keyword"))
                .setSize(0)
                .get();
        StringTerms popular_colors = (StringTerms) response.getAggregations().asMap().get("popular_colors");
        Iterator<StringTerms.Bucket> teamBucketIt = popular_colors.getBuckets().iterator();
        while (teamBucketIt .hasNext()) {
            StringTerms.Bucket buck = teamBucketIt.next();
            String team = (String) buck.getKey();
            long count = buck.getDocCount();
            System.out.print(team+":");
            System.out.println(count);
        }
    }


    /**
     * 手动生成JSON
     */
    @Test
    public void CreateJSON(){

        String json = "{" +
                "\"user\":\"fendo\"," +
                "\"postDate\":\"2013-01-30\"," +
                "\"message\":\"Hell word\"" +
                "}";

        IndexResponse response = helper.getClient().prepareIndex("fendo", "fendodate")
                .setSource(json)
                .get();
        System.out.println(response.getResult());

    }

    @Test
    public void DeleteByQueryAPI(){
        DeleteByQueryAction.INSTANCE.newRequestBuilder(helper.getClient())
                .filter(QueryBuilders.matchQuery("user", "fendo"))      //查询
                .source("fendo")                //index(索引名)
                .execute(new ActionListener<BulkByScrollResponse>() {     //回调监听
                    @Override
                    public void onResponse(BulkByScrollResponse response) {
                        long deleted = response.getDeleted();   //删除文档的数量
                        System.out.println(deleted);
                    }
                    @Override
                    public void onFailure(Exception e) {
                        // Handle the exception
                    }
                });
    }


}