package com.example.a4;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

@Slf4j
public class Bean1 {
    private Bean2 bean2;
    @Autowired
    private Bean3 bean3;
    @Autowired
    public void setBean2(Bean2 bean2){
        log.debug("@Autowired生效"+bean2);
        this.bean2=bean2;
    }
    @Resource
    public void setBean3(Bean3 bean3){
        log.debug("@Resource生效"+bean3);
        this.bean3=bean3;
    }
    @Autowired
    public void setValue(@Value("${java_home}") String home){
        log.debug("@value生效"+home);
    }
    @PostConstruct
    public void init(){
        log.debug("初始化");
    }
    @PreDestroy
    public void destory(){
        log.debug("销毁");
    }

}
