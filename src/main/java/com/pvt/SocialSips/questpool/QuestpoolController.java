package com.pvt.SocialSips.questpool;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/questpool")
public class QuestpoolController {

    @GetMapping("/{userId}")
    public String getByUserId(@PathVariable String userId) {
        return userId;
    }


}
