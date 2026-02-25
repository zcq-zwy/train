package com.zcqzwy.member.controller;


import com.zcqzwy.common.context.LoginMemberContext;
import com.zcqzwy.common.resp.CommonResp;
import com.zcqzwy.common.resp.PageResp;
import com.zcqzwy.member.req.PassengerQueryReq;
import com.zcqzwy.member.req.PassengerSaveReq;
import com.zcqzwy.member.resp.PassengerQueryResp;
import com.zcqzwy.member.service.PassengerService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    @GetMapping("/query-list")
    public CommonResp<PageResp<PassengerQueryResp>> queryList(@Valid PassengerQueryReq req) {
        req.setMemberId(LoginMemberContext.getId());
        PageResp<PassengerQueryResp> list = passengerService.queryList(req);
        return new CommonResp<>(list);
    }



}
