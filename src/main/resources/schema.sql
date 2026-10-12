-- Create contracts table if not exists
CREATE TABLE IF NOT EXISTS contracts (
    id BIGINT NOT NULL AUTO_INCREMENT,
    file_name VARCHAR(255) NOT NULL,
    file_type VARCHAR(100),
    file_size BIGINT,
    extracted_text LONGTEXT,
    risk_score INT,
    status VARCHAR(50),
    uploaded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);