package com.pvt.SocialSips.questpool;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/questpool")
public class QuestpoolController {

    @GetMapping("/{userId}")
    public String getByUserId(@PathVariable String userId) {
        return userId;
    }

    @PostMapping("/{userId}")
    public HttpStatus addQuestpool(@PathVariable String userId) {
        return HttpStatus.ACCEPTED;
    }

}
