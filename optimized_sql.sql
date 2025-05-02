-- 1. 近三個月使用兩種身分登入會員客戶ID(不重複)
-- 找出同時擁有企業/個人戶和信用卡身份，並且近三個月內兩種身份都有登入的客戶
SELECT DISTINCT
    c.COMPANY_KEY,
    c.COMPANY_UID,
    c.COMPANY_KIND
FROM 
    COMPANY c
WHERE 
    -- 確保客戶同時擁有企業/個人戶和信用卡身份
    c.COMPANY_UID IN (
        SELECT 
            COMPANY_UID 
        FROM 
            COMPANY
        GROUP BY 
            COMPANY_UID 
        HAVING 
            MAX(CASE WHEN COMPANY_KIND IN(1, 2) THEN 1 ELSE 0 END) = 1 
            AND MAX(CASE WHEN COMPANY_KIND IN(3, 4) THEN 1 ELSE 0 END) = 1
    )
    AND EXISTS (
        -- 近三個月有使用企業/個人戶身份登入
        SELECT 1
        FROM COMPANY c1
        JOIN eb_login_log_b_bak l1 ON c1.COMPANY_KEY = l1.COMPANY_KEY
        WHERE c1.COMPANY_UID = c.COMPANY_UID
        AND c1.COMPANY_KIND IN (1, 2)
        AND l1.LOGIN_TIME >= ADD_MONTHS(SYSDATE, -3)
    )
    AND EXISTS (
        -- 近三個月有使用信用卡身份登入
        SELECT 1
        FROM COMPANY c2
        JOIN eb_login_log_b_bak l2 ON c2.COMPANY_KEY = l2.COMPANY_KEY
        WHERE c2.COMPANY_UID = c.COMPANY_UID
        AND c2.COMPANY_KIND IN (3, 4)
        AND l2.LOGIN_TIME >= ADD_MONTHS(SYSDATE, -3)
    );

-- 2. 近三個月僅使用信用卡會員身分登入的ID(不重複)
-- 找出同時擁有企業/個人戶和信用卡身份，但近三個月只使用信用卡身份登入的客戶
SELECT DISTINCT
    c.COMPANY_KEY,
    c.COMPANY_UID,
    c.COMPANY_KIND
FROM 
    COMPANY c
WHERE 
    c.COMPANY_KIND IN (3, 4)  -- 只選取信用卡身份的記錄
    -- 確保客戶同時擁有企業/個人戶和信用卡身份
    AND c.COMPANY_UID IN (
        SELECT 
            COMPANY_UID 
        FROM 
            COMPANY
        GROUP BY 
            COMPANY_UID 
        HAVING 
            MAX(CASE WHEN COMPANY_KIND IN(1, 2) THEN 1 ELSE 0 END) = 1 
            AND MAX(CASE WHEN COMPANY_KIND IN(3, 4) THEN 1 ELSE 0 END) = 1
    )
    -- 確保近三個月有使用信用卡身份登入
    AND EXISTS (
        SELECT 1
        FROM eb_login_log_b_bak l
        WHERE l.COMPANY_KEY = c.COMPANY_KEY
        AND l.LOGIN_TIME >= ADD_MONTHS(SYSDATE, -3)
    )
    -- 確保近三個月未使用企業/個人戶身份登入
    AND NOT EXISTS (
        SELECT 1
        FROM COMPANY c2
        JOIN eb_login_log_b_bak l2 ON c2.COMPANY_KEY = l2.COMPANY_KEY
        WHERE c2.COMPANY_UID = c.COMPANY_UID
        AND c2.COMPANY_KIND IN (1, 2)
        AND l2.LOGIN_TIME >= ADD_MONTHS(SYSDATE, -3)
    );

-- 3. 近六個月未登入Fubon+客戶ID(不重複)
-- 找出同時擁有企業/個人戶和信用卡身份，但近六個月未通過Fubon+登入的客戶
SELECT DISTINCT
    c.COMPANY_UID
FROM 
    COMPANY c
WHERE 
    -- 確保客戶同時擁有企業/個人戶和信用卡身份
    c.COMPANY_UID IN (
        SELECT 
            COMPANY_UID 
        FROM 
            COMPANY
        GROUP BY 
            COMPANY_UID 
        HAVING 
            MAX(CASE WHEN COMPANY_KIND IN(1, 2) THEN 1 ELSE 0 END) = 1 
            AND MAX(CASE WHEN COMPANY_KIND IN(3, 4) THEN 1 ELSE 0 END) = 1
    )
    -- 確保近六個月未通過Fubon+(CHANNEL_ID='M')登入
    AND NOT EXISTS (
        SELECT 1
        FROM eb_login_log_b_bak l
        WHERE l.COMPANY_KEY = c.COMPANY_KEY
        AND l.CHANNEL_ID = 'M'  -- Fubon+通路
        AND l.LOGIN_TIME >= ADD_MONTHS(SYSDATE, -6)
    );