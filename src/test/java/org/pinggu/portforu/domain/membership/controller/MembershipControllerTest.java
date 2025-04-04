package org.pinggu.portforu.domain.membership.controller;

import org.mockito.Mock;
import org.pinggu.portforu.config.JwtUtil;
import org.pinggu.portforu.config.SecurityConfig;
import org.pinggu.portforu.domain.membership.service.MembershipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(MembershipController.class)
@Import({SecurityConfig.class, JwtUtil.class})
@WithMockUser
class MembershipControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private MembershipService membershipService;
}