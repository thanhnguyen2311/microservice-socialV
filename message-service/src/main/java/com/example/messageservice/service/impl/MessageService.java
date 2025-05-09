package com.example.messageservice.service.impl;

import com.example.messageservice.dto.*;
import com.example.messageservice.entity.Conversation;
import com.example.messageservice.entity.ConversationMembers;
import com.example.messageservice.entity.Message;
import com.example.messageservice.entity.SocialVUser;
import com.example.messageservice.repository.IConversationMembersRepository;
import com.example.messageservice.repository.IConversationRepository;
import com.example.messageservice.repository.IMessageRepository;
import com.example.messageservice.service.IMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MessageService implements IMessageService {
    @Autowired
    private IMessageRepository messageRepository;
    @Autowired
    private IConversationRepository conversationRepository;
    @Autowired
    private IConversationMembersRepository conversationMembersRepository;

    @Override
    public BaseResponse<Object> findAllByConversation(Long id, BaseResponse<Object> rp) {
        rp.setData(messageRepository.findAllByConversationId(id));
        return rp;
    }

    @Override
    public BaseResponse<Object> findMemberListInChat(Long id, BaseResponse<Object> rp) {
        List<UserInfoDTO> users = conversationMembersRepository.findMemberListInChat(id);
        rp.setData(users);
        return rp;
    }

    @Override
    public BaseResponse<Object> createConversation(CreateConversationReq rq, BaseResponse<Object> rp) {
        Conversation conversation = new Conversation();
        conversation.setType(rq.getType());
        conversation.setCreatedDate(new Date());
        conversation.setAvatarConversationUrl("https://i0.wp.com/sbcf.fr/wp-content/uploads/2018/03/sbcf-default-avatar.png?ssl=1");
        conversation.setCreatedBy(rq.getCreatedBy());
        Conversation conversationSave = conversationRepository.save(conversation);
        List<ConversationMembers> conversationMembersList = new ArrayList<>();
        rq.getUserIds().forEach(userId -> {
            ConversationMembers conversationMembers = new ConversationMembers();
            conversationMembers.setUserId(userId);
            conversationMembers.setConversation(conversationSave);
            conversationMembers.setIsActive(1);
            conversationMembers.setIsSeen(0);
            conversationMembersList.add(conversationMembers);
        });
        if (!conversationMembersList.isEmpty()) {
            conversationMembersRepository.saveAll(conversationMembersList);
        }
        return rp;
    }

    @Override
    public BaseResponse<Object> createMessage(CreateMessageReq rq, BaseResponse<Object> rp) {
        Message message = new Message();
        message.setConversationId(rq.getConversionId());
        message.setUserId(rq.getUserId());
        message.setCreatedDate(new Date());
        message.setContent(rq.getContent());
        Message messageSave = messageRepository.save(message);

        conversationRepository.updateLastMessageDate(rq.getConversionId(), new Date());

        List<ConversationMembers> conversationMembersList = conversationMembersRepository.findAllByConversationId(rq.getConversionId());
        conversationMembersList.forEach(member -> {
            if (member.getUserId().equals(rq.getUserId())) {
                member.setIsSeen(1);
            } else {
                member.setIsSeen(0);
            }
        });
        conversationMembersRepository.saveAll(conversationMembersList);
        return rp;
    }

    @Override
    public BaseResponse<Object> checkExistPersonalConversation(CheckExistPersonalConvRequest rq, BaseResponse<Object> rp) {
        Integer check = conversationRepository.checkExistPersonalConversation(rq.getFirstUser(), rq.getSecondUser());
        if (check == 1) {
            rp.setCode("01");
            rp.setMessage("Existed conversation");
        }
        return rp;
    }

    public BaseResponse<Object> findAllConversationByUser(ConversationListReq rq, BaseResponse<Object> rp) {
        List<Conversation> conversations = conversationRepository.findConversationsByUserIdAndType(rq.getUserId(), rq.getType());
        rp.setData(conversations);
        return rp;
    }

    @Override
    public void updateGroupChatName(Long id, String name) {
        Conversation conversation = conversationRepository.findById(id).get();
        conversation.setName(name);
        conversationRepository.save(conversation);
    }
}
