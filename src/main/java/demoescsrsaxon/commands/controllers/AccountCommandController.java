package demoescsrsaxon.commands.controllers;

import demoescsrsaxon.commands.dtos.AddNewAccountRequestDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/commands")
public class AccountCommandController {
    @PostMapping("/add")
    public String addNewAccount(@RequestBody AddNewAccountRequestDTO request){

    }
}
