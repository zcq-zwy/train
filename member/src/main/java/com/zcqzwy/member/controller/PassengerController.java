package com.zcqzwy.member.controller;


import com.zcqzwy.common.resp.CommonResp;
import com.zcqzwy.member.req.PassengerQueryReq;
import com.zcqzwy.member.req.PassengerSaveReq;
import com.zcqzwy.member.service.PassengerService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/passenger")
public class PassengerController {

    @Resource
    private PassengerService passengerService;

    @PostMapping("/save")
    public CommonResp<Object> save(@Valid @RequestBody PassengerSaveReq req) {
        passengerService.save(req);
        return new CommonResp<>();
    }


}
