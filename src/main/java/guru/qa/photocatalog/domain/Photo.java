package guru.qa.photocatalog.domain;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

public record Photo(String description,
                    Date lastModifyDate,
                    String content) {
}
