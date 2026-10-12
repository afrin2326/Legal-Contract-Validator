package com.legalvalidator.repository;

import com.legalvalidator.model.Contract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.List;

@Repository
public class ContractRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Contract save(Contract contract) {
        String sql = "INSERT INTO contracts " +
                "(file_name, file_type, file_size, extracted_text, risk_score, status, uploaded_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, contract.getFileName());
            ps.setString(2, contract.getFileType());
            ps.setLong(3, contract.getFileSize() == null ? 0L : contract.getFileSize());
            ps.setString(4, contract.getExtractedText());
            ps.setInt(5, contract.getRiskScore() == null ? 0 : contract.getRiskScore());
            ps.setString(6, contract.getStatus());
            ps.setTimestamp(7, Timestamp.valueOf(
                    contract.getUploadedAt() != null
                            ? contract.getUploadedAt()
                            : java.time.LocalDateTime.now()));
            return ps;
        }, keyHolder);

        Number key = keyHolder.getKey();
        if (key != null) {
            contract.setId(key.longValue());
        }
        return contract;
    }

    public Contract findById(Long id) {
        String sql = "SELECT * FROM contracts WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new ContractRowMapper(), id);
    }

    public List<Contract> findAll() {
        String sql = "SELECT * FROM contracts ORDER BY uploaded_at DESC";
        return jdbcTemplate.query(sql, new ContractRowMapper());
    }

    public void updateStatus(Long id, String status) {
        String sql = "UPDATE contracts SET status = ? WHERE id = ?";
        jdbcTemplate.update(sql, status, id);
    }

    private static class ContractRowMapper implements RowMapper<Contract> {
        @Override
        public Contract mapRow(ResultSet rs, int rowNum) throws SQLException {
            Contract c = new Contract();
            c.setId(rs.getLong("id"));
            c.setFileName(rs.getString("file_name"));
            c.setFileType(rs.getString("file_type"));
            c.setFileSize(rs.getLong("file_size"));
            c.setExtractedText(rs.getString("extracted_text"));
            c.setRiskScore(rs.getInt("risk_score"));
            c.setStatus(rs.getString("status"));
            Timestamp ts = rs.getTimestamp("uploaded_at");
            if (ts != null) {
                c.setUploadedAt(ts.toLocalDateTime());
            }
            return c;
        }
    }
}