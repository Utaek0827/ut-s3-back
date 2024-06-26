package com.uts3back.service;

import com.uts3back.dto.UserTotalServiceDTO;
import com.uts3back.dto.UsersServiceDTO;
import com.uts3back.mapper.UserTotalServiceMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Service
public class UserTotalServiceService {

    private final UserTotalServiceMapper userTotalServiceMapper;

    public UserTotalServiceService(UserTotalServiceMapper userTotalServiceMapper, UserTotalServiceMapper userTotalServiceMapper1) {
        this.userTotalServiceMapper = userTotalServiceMapper;
    }

    // 회원가입 시 기본 1기가 + 1년으로 생성
    public void insertUserTotalService(String email){
        String usID = String.valueOf(UUID.randomUUID());
        LocalDate today = LocalDate.now();
        LocalDate nextYear = today.plusYears(1);

        UserTotalServiceDTO userTotalServiceDTO = new UserTotalServiceDTO();
        userTotalServiceDTO.setUsID(usID);
        userTotalServiceDTO.setEmail(email);
        userTotalServiceDTO.setUserTotalCap(1073741824); // 기본 1기가
        userTotalServiceDTO.setUserUsageCap(0);

        userTotalServiceDTO.setUserStartDate(today);
        userTotalServiceDTO.setUserEndDate(nextYear);

        userTotalServiceMapper.insertUserTotalService(userTotalServiceDTO);

    }

    // 토탈서비스 날짜 체크
    public int CheckUserLicense(String email, Date today){
        return userTotalServiceMapper.countByEmailAndValidServiceDate(email, today);
    }

    public UserTotalServiceDTO getUserTotalService(String email) {
        UserTotalServiceDTO userTotalServiceDTO = userTotalServiceMapper.userTotalServiceFind(email);
        Integer size = userTotalServiceMapper.calculateTotalImageSize(email);
        userTotalServiceDTO.setUserUsageCap(size);

        //토탈서비스 생성

        return userTotalServiceDTO;

    }

    public boolean correctionImgSize(Map res) {
        UserTotalServiceDTO userTotalServiceDTO = userTotalServiceMapper.userTotalServiceFind((String)res.get("email"));

        System.out.println(res.get("imgOriSize"));
        long correctionSize = 0;

        // 업로드 -- 0, 수정 -- 1, 삭제 -- 2
        int kind = (int)res.get("kind");

        // 업로드 일 경우 기존용량에 이미지 크기 더하기
        // 수정일 경우 원래 파일크기 빼고 새로운 파일크기 더하기
        if(kind == 1){
            correctionSize = userTotalServiceDTO.getUserUsageCap() + (long)res.get("imgSize") - (long)res.get("imgOriSize");
        }else if(kind == 0){
            correctionSize = userTotalServiceDTO.getUserUsageCap() + (long)res.get("imgSize");
        }else if(kind == 2){
            correctionSize = userTotalServiceDTO.getUserUsageCap() - (long)res.get("imgOriSize");
        }

        // 전체용량보다 수정된 용량이 크면 false 리턴
        if(userTotalServiceDTO.getUserTotalCap() <= correctionSize){
            return false;
        }

        // 아니면 용량 업데이트 후 true 리턴
        userTotalServiceDTO.setUserUsageCap(correctionSize);
        userTotalServiceMapper.updateUserTotalService(userTotalServiceDTO);

        System.out.println(userTotalServiceDTO);

        return true;



    }
}
