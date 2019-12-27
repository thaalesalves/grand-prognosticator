package es.thalesalv.bot.rpg.bean;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationBeans {

    @Bean
    public GrandPrognosticator grandPrognosticatorBean() {
        return new GrandPrognosticator();
    }

    @Bean
    public PDFUtils pdfBean() {
        return new PDFUtils();
    }

    @Bean
    public Watson watsonBean() {
        return new Watson();
    }

    @Bean
    public YouTube youTubeBean() {
        return new YouTube();
    }
}