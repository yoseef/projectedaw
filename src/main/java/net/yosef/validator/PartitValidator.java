package net.yosef.validator;

import net.yosef.domain.Partit;
import net.yosef.repository.PartitRepository;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by user on 7/06/15.
 */
@Component
public class PartitValidator implements Validator {

    public PartitValidator() {
    }

    @Inject
    private PartitRepository partitRepository;

    @Override
    public boolean supports(Class<?> aClass) {
       return Partit.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        //validem que en aquest dia no hihagi un partit amb els mateixos equips a la mateixa hora:
        Partit p = (Partit)  o;
        //obtenc una llista de tots els equips que jugen el mateix dia.
        List<Partit> partits = partitRepository.findByData(p.getData());

        for(Partit partit : partits){
            if (partit.getEquips().equals(p.getEquips())){
                errors.rejectValue(partit.getEquips().toString(), "Error");
            }
        }



    }

    public void setPartitRepository(PartitRepository partitRepository) {
        this.partitRepository = partitRepository;
    }

}
