package com.qfant.gjp.service;


import com.qfant.gjp.entity.VipCard;
import com.qfant.gjp.repository.VipCardMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class VipCardService {
    @Autowired
    private VipCardMapper vipCardMapper;

    public VipCard getVipCardByCardNo(String CardNo) {
        return vipCardMapper.getVipCardByCardNo(CardNo);
    }
    public void deleteVipCardByCardNo(String CardNo,String Tel){
        vipCardMapper.deleteVipCardByCardNo(CardNo, Tel);
    }

    public void updateVipCard(VipCard vipCard) {
        vipCardMapper.updateVipCard(vipCard);
    }
}
