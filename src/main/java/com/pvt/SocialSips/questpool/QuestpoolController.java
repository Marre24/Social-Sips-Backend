package com.pvt.SocialSips.questpool;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/questpool")
public class QuestpoolController {


    @GetMapping("/{userId}")
    public Long getByUserId(@PathVariable Long userId) {
        return userId;
    }

    @PostMapping("/{userId}")
    public HttpStatus addQuestpool(@PathVariable Long userId) {
        return HttpStatus.ACCEPTED;
    }

}
