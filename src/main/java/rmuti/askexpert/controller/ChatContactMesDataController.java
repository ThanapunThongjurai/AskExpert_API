package rmuti.askexpert.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rmuti.askexpert.model.exception.BaseException;
import rmuti.askexpert.model.mapper.ResChatContactMapper;
import rmuti.askexpert.model.mapper.ResChatMesMapper;
import rmuti.askexpert.model.mapper.ResUserMapper;
import rmuti.askexpert.model.repo.*;
import rmuti.askexpert.model.response.APIResponse;
import rmuti.askexpert.model.response.ResChatContact;
import rmuti.askexpert.model.response.ResChatMes;
import rmuti.askexpert.model.response.ResUserExpertVerify;
import rmuti.askexpert.model.services.TokenService;
import rmuti.askexpert.model.table.ChatContactData;
import rmuti.askexpert.model.table.ChatMesData;
import rmuti.askexpert.model.table.ExpertGroupListData;
import rmuti.askexpert.model.table.VerifyData;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/chat")
public class ChatContactMesDataController {



    @Autowired
    ChatContactRepository chatContactRepository;

    @Autowired
    ChatMesRepository chatMesRepository;


    @Autowired
    private TokenService tokenService;

    @Autowired
    private ResChatContactMapper chatContactMapper;

    @Autowired
    private ResChatMesMapper resChatMesMapper;

    @Autowired
    private ResUserMapper resUserMapper;

    @Autowired
    UserInfoRepository userInfoRepository;

    @Autowired
    private ExpertGroupDataRepository expertGroupDataRepository;

    @Autowired
    private VerifyDataRepository verifyDataRepository;


    private ResUserExpertVerify createUserDisplay(ResUserExpertVerify resUserExpertVerify) {
        Optional<ExpertGroupListData> expertGroupListData = expertGroupDataRepository.findById(resUserExpertVerify.getExpertGroupId());
        Optional<VerifyData> verifyData = verifyDataRepository.findByVerifyFrom(resUserExpertVerify.getUserInfoId());
        if (expertGroupListData.isPresent()) {
            resUserExpertVerify.setExpertGroupListData(expertGroupListData.get());
        }
        if (verifyData.isPresent()) {
            resUserExpertVerify.setVerifyData(verifyData.get());
        }
        return resUserExpertVerify;
    }

    //firstContract
    @PostMapping("/firstContact")
    public Object firstContact(@RequestBody String chatRx) throws BaseException{
        APIResponse res = new APIResponse();
        String userId = tokenService.userId();

        //findConcat
        Optional<ChatContactData> chatContactTx = chatContactRepository.findByChatTxAndChatRx(userId,chatRx);
        //Optional<ChatContactData> chatContactRx = chatContactRepository.findByChatTxAndChatRx(chatRx,userId);


        //if null Create
        if(chatContactTx.isEmpty() /*&& chatContactRx.isEmpty()*/)
        {
            ChatContactData newChatContactTx =  new ChatContactData();
            newChatContactTx.setChatTx(userId);
            newChatContactTx.setChatRx(chatRx);
            newChatContactTx.setChatTxReadStatus(false);
            newChatContactTx.setChatRxReadStatus(false);
//             ChatContactData newChatContactRx =  new ChatContactData();
//            newChatContactRx.setChatTx(chatRx);
//            newChatContactRx.setChatRx(userId);
//            newChatContactRx.setChatReadStatus(false);
            chatContactRepository.save(newChatContactTx);
            //chatContactRepository.save(newChatContactRx);
             res.setData(newChatContactTx);
        }
        else {
            res.setData(chatContactTx.get());
        }
        return res;
    }

    @PostMapping("/myContact")
    public Object myContact() throws BaseException{
        APIResponse res = new APIResponse();
        String userId = tokenService.userId();
        List<ResChatContact> chatContactTx = chatContactMapper.toListResChatContact(chatContactRepository.findByChatTx(userId));

        //SwitchSideTxRx
        for(ResChatContact data : chatContactTx){
            if(userId != data.getChatTx()){
                String tempTx = data.getChatTx();
                String tempRx = data.getChatRx();
                data.setChatRx(tempTx);
                data.setChatTx(tempRx);
                data.setUserInfoDataRx(resUserMapper.toResUserExpertVerify(userInfoRepository.findById(data.getChatRx()).get()));
                data.setUserInfoDataRx(createUserDisplay(data.getUserInfoDataRx()));
            }
        }


        res.setData(chatContactTx);
        return res;
    }

//    @PostMapping("/itRead")
//    public Object itRead(@RequestBody String chatRx) throws BaseException{
//        APIResponse res = new APIResponse();
//        String userId = tokenService.userId();
//        Optional<ChatContactData> chatContactRx = chatContactRepository.findByChatTxAndChatRx(userId,chatRx);
//        if(chatContactRx.isPresent()){
//            chatContactRx.get().setChatReadStatus(true);
//            chatContactRepository.save(chatContactRx.get());
//        }
//        return res;
//    }

    //AddMes-SendMes-CreateMes

    @PostMapping("/sendMes")
    public Object send(@RequestBody ChatMesData temp)throws BaseException{
        APIResponse res = new APIResponse();
        String userId = tokenService.userId();
        ChatMesData mesData = new ChatMesData();
        mesData.setChatTx(userId);
        mesData.setChatContactId(temp.getChatContactId());
        mesData.setChatMes(temp.getChatMes());
        chatMesRepository.save(mesData);
        res.setData(mesData);
        return res;

    }

    @PostMapping("/chatMesWithRx")
    public Object chatMesWithRx(@RequestBody String contactId)throws BaseException{
        APIResponse res = new APIResponse();
        String userId = tokenService.userId();
        List<ResChatMes> chatMesDataList =  resChatMesMapper.toListResChatMes(chatMesRepository.findByChatContactIdOrderByCreatedDateAsc(contactId));
        res.setData(chatMesDataList);
        return res;

    }
}
