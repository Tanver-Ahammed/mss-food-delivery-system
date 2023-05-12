package com.food.delivery.controller;

import com.food.delivery.dto.FoodDTO;
import com.food.delivery.dto.UserDTO;
import com.food.delivery.services.impl.FoodServiceImpl;
import com.food.delivery.services.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;

@Controller
@RequestMapping(path = "/food")
public class FoodController {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private FoodServiceImpl foodService;

    @GetMapping(path = "/add")
    public String addFood(Model model, Principal principal) {
        UserDTO user = this.userService.getUserDTOIfLoggedIn(principal);
        model.addAttribute("name", user.getName());

        model.addAttribute("foodDTO", new FoodDTO());
        model.addAttribute("message", "");
        return "authenticated/food/add-food";
    }

    @PostMapping(path = "/save")
    public String saveFood(@Valid @ModelAttribute("foodDTO") FoodDTO foodDTO, BindingResult result,
                           @RequestParam("foodImage") MultipartFile foodImage,
                           Model model, Principal principal) throws IOException {
        UserDTO user = this.userService.getUserDTOIfLoggedIn(principal);
        model.addAttribute("name", user.getName());


        FoodDTO resultFoodDTO = this.foodService
                .addFood(foodDTO, foodImage);

        model.addAttribute("foodDTO", new FoodDTO());
        model.addAttribute("message", "Food is successfully added...");
        return "authenticated/food/add-food";
    }

    // get all user
    @GetMapping(path = "/all")
    public String getAllFood(Model model, Principal principal) {
        UserDTO user = this.userService.getUserDTOIfLoggedIn(principal);
        model.addAttribute("name", user.getName());

        model.addAttribute("foodDTOS", this.foodService.getAllFood());
        return "authenticated/food/show-all-food";
    }

    // get user by id
    @GetMapping(path = "/get/{foodId}")
    public String getFoodById(@PathVariable("foodId") Long foodId,
                              Model model, Principal principal) {
        UserDTO user = this.userService.getUserDTOIfLoggedIn(principal);
        model.addAttribute("name", user.getName());

        model.addAttribute("foodDTO", this.foodService.getSingleFoodById(foodId));
        return "authenticated/food/show-single-food";
    }

}
