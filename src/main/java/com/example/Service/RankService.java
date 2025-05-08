package com.example.Service;

import com.example.Entity.Rank;
import com.example.Repository.RankRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RankService {

    @Autowired
    private RankRepository rankRepository;

    // Tạo mới một Rank
    public Rank saveRank(Rank rank) {
        return rankRepository.save(rank);
    }

    // Lấy tất cả các Rank
    public List<Rank> getAllRanks() {
        return rankRepository.findAll();
    }

    // Lấy Rank theo ID
    public Optional<Rank> getRankById(int id) {
        return rankRepository.findById(id);
    }

    // Xóa Rank
    public void deleteRank(int id) {
        rankRepository.deleteById(id);
    }
}
