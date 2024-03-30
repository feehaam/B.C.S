package com.bcs.analyzer.service.v1;

import com.bcs.analyzer.model.Ban;
import com.bcs.analyzer.repository.BanRepository;
import com.bcs.analyzer.util.Cache;
import com.bcs.analyzer.util.ID;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.bcs.analyzer.util.Cache.allBansAsString;

@Service @RequiredArgsConstructor
public class BanService {

    private final BanRepository banRepository;
    private final ID id;

    @PostConstruct
    private void init(){
        reloadBanAndId();
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
        reloadBanAndId();
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
        reloadBanAndId();
        return result;
    }

    public void delete(Integer id){
        banRepository.deleteById(id);
        reloadBanAndId();
    }

    private void reloadBanAndId(){
        List<Ban> bans = banRepository.findAll();
        bans.forEach(ban -> {
            id.lastBanId = Math.max(id.lastBanId, ban.getId());
        });
        Cache.initAllBansAndId(bans);
    }

}
