package com.roncoo.es.senior;

import java.net.InetAddress;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

/**
 * 课程88 对汽车品牌进行全文检索、精准查询和前缀搜索
 */
public class FullTextSearchByBrand {
	
	@SuppressWarnings({ "resource", "unchecked" })
	public static void main(String[] args) throws Exception {
		Settings settings = Settings.builder()
				.put("cluster.name", "elasticsearch")
				.build();
		
		TransportClient client = new PreBuiltTransportClient(settings)
				.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));  

		//matchQuery 全文检索 会分词
		SearchResponse searchResponse = client.prepareSearch("car_shop")
				.setTypes("cars")
				.setQuery(QueryBuilders.matchQuery("brand", "宝马"))
				.get();
		
		for(SearchHit searchHit : searchResponse.getHits().getHits()) {
			System.out.println(searchHit.getSourceAsString());  
		}
		
		System.out.println("====================================================");

		//multiMatchQuery 基于多个关键词 全文检索
		searchResponse = client.prepareSearch("car_shop")
				.setTypes("cars")
				.setQuery(QueryBuilders.multiMatchQuery("宝马", "brand", "name"))  
				.get();
		
		for(SearchHit searchHit : searchResponse.getHits().getHits()) {
			System.out.println(searchHit.getSourceAsString());  
		}
		
		System.out.println("====================================================");

		//termQuery 全文检索 不会分词
		searchResponse = client.prepareSearch("car_shop")
				.setTypes("cars")
				.setQuery(QueryBuilders.termQuery("name.raw", "宝马318"))    
				.get();
		
		for(SearchHit searchHit : searchResponse.getHits().getHits()) {
			System.out.println(searchHit.getSourceAsString());  
		}
		
		System.out.println("====================================================");

		//prefixQuery 前缀检索
		searchResponse = client.prepareSearch("car_shop")
				.setTypes("cars")
				.setQuery(QueryBuilders.prefixQuery("name", "宝"))      
				.get();
		
		for(SearchHit searchHit : searchResponse.getHits().getHits()) {
			System.out.println(searchHit.getSourceAsString());  
		}
		
		client.close();
	}
	
}
