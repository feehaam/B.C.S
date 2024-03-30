//package com.bcs.analyzer.controller.v1;
//
//import com.bcs.analyzer.model.Ban;
//import com.bcs.analyzer.service.v1.BanService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/bans")
//public class BanController {
//
//    @Autowired
//    private BanService banService;
//
//    @GetMapping("/{id}")
//    public ResponseEntity<?> getBanById(@PathVariable("id") Integer id) {
//        Ban ban = banService.getBanById(id);
//        if (ban == null) {
//            return ResponseEntity.notFound().build();
//        }
//        return ResponseEntity.ok(ban);
//    }
//
//    @GetMapping
//    public ResponseEntity<List<Ban>> getAllBans() {
//        List<Ban> bans = banService.getAllBan();
//        return ResponseEntity.ok(bans);
//    }
//
//    @PostMapping
//    public ResponseEntity<Ban> createBan(@RequestBody String word) {
//        Ban ban = banService.create(word);
//        return ResponseEntity.ok(ban);
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<?> updateBan(@PathVariable("id") Integer id, @RequestBody String word) {
//        Ban updatedBan = banService.update(id, word);
//        if (updatedBan == null) {
//            return ResponseEntity.notFound().build();
//        }
//        return ResponseEntity.ok(updatedBan);
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<String> deleteBan(@PathVariable("id") Integer id) {
//        banService.delete(id);
//        return ResponseEntity.status(HttpStatus.OK).body("Ban deleted successfully");
//    }
//}
