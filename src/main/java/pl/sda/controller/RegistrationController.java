package pl.sda.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import pl.sda.bussiness.UserBoImpl;
import pl.sda.bussiness.UserValidator;
import pl.sda.dto.UserDto;

import javax.validation.Valid;

@Controller
public class RegistrationController {

    private static final String USER_REGISTRED_CORRECTLY = "Użytkownik zarejestrowany poprawnie";
    @Autowired
    private UserBoImpl userBo;

    @Autowired
    private UserValidator validator;

    @GetMapping("/register")
    public String page(Model model) {
        initModel(model);
        return "registration";
    }

    @PostMapping("/register")
    public String saveUser(@Valid @ModelAttribute(name = "user") UserDto user, BindingResult bindingResult,
                           Model model) {
        if (bindingResult.hasErrors() || validate(user, model)) {
            return "registration";
        }

        userBo.saveUser(user);
        model.addAttribute("userRegisteredCorrectly", USER_REGISTRED_CORRECTLY);
        return "login";
    }

    private boolean validate(UserDto user, Model model) {
        String result = validator.notValid(user);
        if (result != null) {
            model.addAttribute("commonError", result);
        }
        return result != null;
    }

    private void initModel(Model model) {
        model.addAttribute("user", new UserDto());
    }

}