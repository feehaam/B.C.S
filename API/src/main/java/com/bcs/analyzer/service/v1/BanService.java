package com.bcs.analyzer.service.v1;

import com.bcs.analyzer.model.Ban;
import com.bcs.analyzer.repository.BanRepository;
import com.bcs.analyzer.util.Cache;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.bcs.analyzer.util.Cache.allBansAsString;

@Service @RequiredArgsConstructor
public class BanService {

    private final BanRepository banRepository;

    @PostConstruct
    private void loadBans(){
        reloadBan();
    }

    @PostConstruct
    void addData(){
        create("........");
    }

    public Ban getBanById(Integer id){
        Optional<Ban> ban = banRepository.findById(id);
        return ban.orElse(null);
    }

    public List<Ban> getAllBan(){
        return banRepository.findAll();
    }

    public Ban create(String word){
        word = word.toLowerCase();
        Ban result = banRepository.save(new Ban(0, word));
        reloadBan();
        return result;
    }

    public List<Ban> createBatch(List<String> bans){
        List<Ban> newBans = new ArrayList<>();
        bans.forEach(word -> {
            if (!allBansAsString.contains(word.toLowerCase())){
                newBans.add(new Ban(0, word.toLowerCase()));
            }
        });
        List<Ban> results = banRepository.saveAll(newBans);
        allBansAsString.addAll(bans);
        return results;
    }

    public Ban update(Integer id, String word){
        Optional<Ban> banOp = banRepository.findById(id);
        if(banOp.isEmpty()) return null;
        Ban ban = banOp.get();
        ban.setWord(word);
        Ban result = banRepository.save(ban);
        reloadBan();
        return result;
    }

    public void delete(Integer id){
        banRepository.deleteById(id);
        reloadBan();
    }

    private void reloadBan(){
        Cache.initAllBans(banRepository.findAll());
    }
}
