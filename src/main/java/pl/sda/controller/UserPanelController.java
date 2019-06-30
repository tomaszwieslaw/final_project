package pl.sda.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
public class UserPanelController {

    private static final String USER_REGISTRED_CORRECTLY = "Użytkownik zarejestrowany poprawnie";
    @Autowired
    private UserBoImpl userBo;

    @Autowired
    private UserValidator validator;

    @GetMapping("/userPanel")
    public String page(@Valid @ModelAttribute(name = "user") UserDto user, BindingResult bindingResult, Model model) {
        String username;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }
        model.addAttribute("user", userBo.getUser(username));

        return "userPanel";
    }

    @PostMapping("/saveUserPanel")
    public String saveUser(@Valid @ModelAttribute(name = "user") UserDto user, BindingResult bindingResult,
                           Model model) {
        if (bindingResult.hasErrors() || validate(user, model)) {
            return "userPanel";
        }

        userBo.saveUser(user);
        model.addAttribute("userRegisteredCorrectly", USER_REGISTRED_CORRECTLY);
        return "userPanel";
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
