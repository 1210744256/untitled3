package com.example.a28;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.mock.http.MockHttpInputMessage;
import org.springframework.mock.http.MockHttpOutputMessage;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class A28 {
    @Test
    public void test3() throws IOException {
        MockHttpInputMessage message = new MockHttpInputMessage("""
                {
                "name":"小白",
                "age":"18"
             
                }""".getBytes(StandardCharsets.UTF_8));
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        if (converter.canRead(User.class, MediaType.APPLICATION_JSON)) {
            Object read =  converter.read(User.class, message);
            System.out.println(read);
        }
    }
    @Test
    public void test2() throws IOException {
        MockHttpOutputMessage message = new MockHttpOutputMessage();
        MappingJackson2XmlHttpMessageConverter converter = new MappingJackson2XmlHttpMessageConverter();
        if(converter.canWrite(User.class,MediaType.APPLICATION_XML)){
            converter.write(new User(16,"帝君好"),MediaType.APPLICATION_XML,message);
            System.out.println(message.getBodyAsString());
        }
    }
    @Test
    public void test1() throws IOException {
//        将对象数据转json
        MockHttpOutputMessage message = new MockHttpOutputMessage();
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
//        如果能把user对象转为json
        if (converter.canWrite(User.class, MediaType.APPLICATION_JSON)) {
            converter.write(new User(15, "小白"), MediaType.APPLICATION_JSON, message);
//            System.out.println(1);
            System.out.println(message.getBodyAsString());
        }
    }

    @Data
    static class User {
        private Integer age;
        private String name;

        @JsonCreator
        public User(@JsonProperty("age") Integer age, @JsonProperty("name") String name) {
            this.age = age;
            this.name = name;
        }
    }
}
