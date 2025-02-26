-- 创建系统设置表
CREATE TABLE IF NOT EXISTS system_settings (
    id INT AUTO_INCREMENT PRIMARY KEY,
    setting_key VARCHAR(50) NOT NULL UNIQUE,
    setting_value VARCHAR(255) NOT NULL,
    description VARCHAR(255)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 插入默认设置
INSERT INTO system_settings (setting_key, setting_value, description) VALUES
('table_count', '20', '餐厅桌位数量'),
('delivery_fee', '5', '外卖配送费（元）')
ON DUPLICATE KEY UPDATE setting_value = VALUES(setting_value); 