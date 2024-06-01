package at.technikum.tourplanner.service.helper;

import at.technikum.tourplanner.persistence.entity.TourEntity;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;


@Component
public class MailGenerator {
    public String parseThymeleafTemplateMailTourDetails(TourEntity tour) {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);

        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);

        Context context = new Context();
        context.setVariable("tour", tour);

        return templateEngine.process("thymeleaf/tour_mail", context);
    }
}
