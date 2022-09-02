package com.atguigu.gmall.search.bean;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * Document:
 *      indexName：索引名
 *      shards：分片数
 *      replicas：副本数
 *
 * Field:
 *      value: ES 中实际的属性名
 *      type：在 ES 保存的类型（如果是 String 类型的话，默认在 ES 中为 text）
 *          text: 存的时候会分词
 *          keyword: 关键字不允许分词
 *          analyzer: 指定分词器
 *
 */
@Data
@Document(indexName = "person",shards = 1,replicas = 1)
public class Person {
    @Id
    private Long id;
    @Field(value = "first",type = FieldType.Keyword)
    private String firstName;
    @Field(value = "last",type = FieldType.Keyword)
    private String lastName;
    @Field(value = "age")
    private Integer age;
    @Field(value = "address",type = FieldType.Text,analyzer = "ik_smart")
    private String address;
}
