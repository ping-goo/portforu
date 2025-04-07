package org.pinggu.portforu.domain.membership.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.pinggu.portforu.common.dto.AuthMember;
import org.pinggu.portforu.config.JwtUtil;
import org.pinggu.portforu.config.SecurityConfig;
import org.pinggu.portforu.domain.membership.dto.request.CreateMembershipRequestDto;
import org.pinggu.portforu.domain.membership.dto.request.UpdateMembershipRequestDto;
import org.pinggu.portforu.domain.membership.dto.response.MembershipResponseDto;
import org.pinggu.portforu.domain.membership.repository.MembershipRepository;
import org.pinggu.portforu.domain.membership.service.MembershipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MembershipController.class)
@Import({SecurityConfig.class, JwtUtil.class})
@WithMockUser
class MembershipControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private MembershipService membershipService;

    @MockitoBean
    private MembershipRepository membershipRepository;

    @Test
    public void 맴버쉽_생성_컨트롤러_테스트() throws Exception {
        // given
        CreateMembershipRequestDto request = new CreateMembershipRequestDto("테스트", 10000, 2000, 2024);
        MembershipResponseDto membershipResponse = new MembershipResponseDto(
                1L, "테스트", 10000, 2000, 2024,
                LocalDateTime.now(), LocalDateTime.now(), null
        );

        given(membershipService.saveMembership(any(AuthMember.class), any(CreateMembershipRequestDto.class)))
                .willReturn(membershipResponse);

        // when & then
        mockMvc.perform(post("/api/v1/memberships")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void 모든_회원조회_컨트롤러_테스트() throws Exception {
        // given
        MembershipResponseDto membership1 = new MembershipResponseDto(1L, "회원1", 10000, 2000, 2024,
                LocalDateTime.now(), LocalDateTime.now(), null);
        MembershipResponseDto membership2 = new MembershipResponseDto(2L, "회원2", 15000, 1500, 2023,
                LocalDateTime.now(), LocalDateTime.now(), null);
        List<MembershipResponseDto> membershipsList = Arrays.asList(membership1, membership2);
        Page<MembershipResponseDto> memberships = new PageImpl<>(membershipsList);

        given(membershipService.findByAllMemberships(any(AuthMember.class), any(Pageable.class)))
                .willReturn(memberships);

        // when & then
        mockMvc.perform(get("/api/v1/memberships")
                        .param("page", "1")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void 특정회원_조회_컨트롤러_테스트() throws Exception {
        // given
        Long membershipId = 1L;
        MembershipResponseDto membershipResponse = new MembershipResponseDto(
                membershipId, "회원1", 10000, 2000, 2024,
                LocalDateTime.now(), LocalDateTime.now(), null
        );

        given(membershipService.findByIdMemberships(any(AuthMember.class), eq(membershipId)))
                .willReturn(membershipResponse);

        // when & then
        mockMvc.perform(get("/api/v1/memberships/{membershipId}", membershipId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void 회원정보_업데이트_컨트롤러_테스트() throws Exception {
        // given
        Long membershipId = 1L;
        UpdateMembershipRequestDto request = new UpdateMembershipRequestDto("업데이트된 회원", 12000, 2500, 2025);
        MembershipResponseDto updatedMembership = new MembershipResponseDto(
                membershipId, "업데이트된 회원", 12000, 2500, 2025,
                LocalDateTime.now(), LocalDateTime.now(), null
        );

        given(membershipService.updateMembership(any(AuthMember.class), eq(membershipId), any(UpdateMembershipRequestDto.class)))
                .willReturn(updatedMembership);

        // when & then
        mockMvc.perform(put("/api/v1/memberships/{membershipId}", membershipId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void 회원정보_삭제_컨트롤러_테스트() throws Exception {
        // given
        Long membershipId = 1L;
        MembershipResponseDto deletedMembership = new MembershipResponseDto(
                membershipId, "삭제된 회원", 10000, 2000, 2024,
                LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now()
        );

        given(membershipService.deleteMembership(any(AuthMember.class), eq(membershipId)))
                .willReturn(deletedMembership);

        // when & then
        mockMvc.perform(delete("/api/v1/memberships/{membershipId}", membershipId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }
}