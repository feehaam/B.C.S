package com.bcs.analyzer.service;

import com.bcs.analyzer.model.Ban;
import com.bcs.analyzer.repository.BanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service @RequiredArgsConstructor
public class BanService {

    private final BanRepository banRepository;

    public Ban getBanById(Integer id){
        Optional<Ban> ban = banRepository.findById(id);
        return ban.orElse(null);
    }

    public List<Ban> getAllBan(){
        return banRepository.findAll();
    }

    public Ban create(String word){
        word = word.toLowerCase();
        return banRepository.save(new Ban(0, word));
    }

    public Ban update(Integer id, String word){
        Optional<Ban> banOp = banRepository.findById(id);
        if(banOp.isEmpty()) return null;
        Ban ban = banOp.get();
        ban.setWord(word);
        return banRepository.save(ban);
    }

    public void delete(Integer id){
        banRepository.deleteById(id);
    }
}
