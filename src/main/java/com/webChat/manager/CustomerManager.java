//package com.webChat.manager;
//
//import com.webChat.dao.TClueDao;
//import com.webChat.dao.TCustomerDao;
//import com.webChat.domain.po.TClue;
//import com.webChat.domain.po.TCustomer;
//import com.webChat.domain.query.CustomerQuery;
//import com.webChat.util.JWTUtils;
//import jakarta.annotation.Resource;
//import org.springframework.beans.BeanUtils;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.Date;
//
//@Component
//public class CustomerManager {
//
//    @Resource
//    private TCustomerDao tCustomerDao;
//
//    @Resource
//    private TClueDao tClueDao;
//
//    @Transactional(rollbackFor = Exception.class)
//    public Boolean convertCustomer(CustomerQuery customerQuery) {
//        //1、验证该线索是否已经转了客户
//        TClue clue = tClueDao.selectByPrimaryKey(customerQuery.getClueId());
//        if (clue.getState() == -1) { //说明该线索已经转成了客户，不能再转了
//            throw new RuntimeException("该线索已经被转成客户，不能再转了");
//        }
//
//        //2、在客户表插入一条客户数据；TCustomerDao
//        TCustomer tCustomer = new TCustomer();
//        BeanUtils.copyProperties(customerQuery, tCustomer);
//        tCustomer.setCreateTime(new Date()); //创建时间
//        //解析jwt得到userId
//        Integer loginUserId = JWTUtils.parseJWTByUserId(customerQuery.getToken());
//        tCustomer.setCreateBy(loginUserId); //创建人
//        int insert = tCustomerDao.insertSelective(tCustomer);
//
//        //3、线索表的state改为-1； TClueDao
//        TClue tClue = new TClue();
//        tClue.setId(customerQuery.getClueId());
//        tClue.setState(-1); //-1表示已转客户
//        int updata = tClueDao.updateByPrimaryKeySelective(tClue);
//
//        return insert >= 1 && updata >= 1;
//    }
//}
