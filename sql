SELECT DISTINCT
    c.COMPANY_KEY,
    c.COMPANY_UID,
    c.COMPANY_KIND
FROM
    DBUSERNEB.COMPANY c
JOIN (
    -- 確認用戶同時擁有兩種身份
    SELECT 
        COMPANY_UID
    FROM 
        DBUSERNEB.COMPANY
    GROUP BY 
        COMPANY_UID
    HAVING 
        MAX(CASE WHEN COMPANY_KIND IN (1, 2) THEN 1 ELSE 0 END) = 1
        AND MAX(CASE WHEN COMPANY_KIND IN (3, 4) THEN 1 ELSE 0 END) = 1
) dual_users ON c.COMPANY_UID = dual_users.COMPANY_UID
WHERE 
    c.COMPANY_KIND IN (1, 2, 3, 4)
    -- 確認用戶有使用企業/個人戶身份登入
    AND EXISTS (
        SELECT 1
        FROM DBUSERNEB.COMPANY c1
        JOIN DBUSERNEB.EB_LOGIN_LOG_B l1 ON c1.COMPANY_KEY = l1.COMPANY_KEY
        WHERE c1.COMPANY_UID = c.COMPANY_UID
        AND c1.COMPANY_KIND IN (1, 2)
        AND l1.LOGIN_TIME >= ADD_MONTHS(SYSDATE, -3)
    )
    -- 確認用戶有使用信用卡身份登入
    AND EXISTS (
        SELECT 1
        FROM DBUSERNEB.COMPANY c2
        JOIN DBUSERNEB.EB_LOGIN_LOG_B l2 ON c2.COMPANY_KEY = l2.COMPANY_KEY
        WHERE c2.COMPANY_UID = c.COMPANY_UID
        AND c2.COMPANY_KIND IN (3, 4)
        AND l2.LOGIN_TIME >= ADD_MONTHS(SYSDATE, -3)
    )
ORDER BY
    c.COMPANY_UID, c.COMPANY_KIND;

SELECT DISTINCT
    c.COMPANY_KEY,
    c.COMPANY_UID,
    c.COMPANY_KIND
FROM
    DBUSERNEB.COMPANY c
JOIN (
    -- 確認用戶同時擁有兩種身份
    SELECT 
        COMPANY_UID
    FROM 
        DBUSERNEB.COMPANY
    GROUP BY 
        COMPANY_UID
    HAVING 
        MAX(CASE WHEN COMPANY_KIND IN (1, 2) THEN 1 ELSE 0 END) = 1
        AND MAX(CASE WHEN COMPANY_KIND IN (3, 4) THEN 1 ELSE 0 END) = 1
) dual_users ON c.COMPANY_UID = dual_users.COMPANY_UID
WHERE 
    c.COMPANY_KIND IN (3, 4)
    -- 確認用戶有使用信用卡身份登入
    AND EXISTS (
        SELECT 1
        FROM DBUSERNEB.COMPANY c1
        JOIN DBUSERNEB.EB_LOGIN_LOG_B l1 ON c1.COMPANY_KEY = l1.COMPANY_KEY
        WHERE c1.COMPANY_UID = c.COMPANY_UID
        AND c1.COMPANY_KIND IN (3, 4)
        AND l1.LOGIN_TIME >= ADD_MONTHS(SYSDATE, -3)
    )
    -- 確認用戶沒有使用企業/個人戶身份登入
    AND NOT EXISTS (
        SELECT 1
        FROM DBUSERNEB.COMPANY c2
        JOIN DBUSERNEB.EB_LOGIN_LOG_B l2 ON c2.COMPANY_KEY = l2.COMPANY_KEY
        WHERE c2.COMPANY_UID = c.COMPANY_UID
        AND c2.COMPANY_KIND IN (1, 2)
        AND l2.LOGIN_TIME >= ADD_MONTHS(SYSDATE, -3)
    )
ORDER BY
    c.COMPANY_UID, c.COMPANY_KIND;

SELECT DISTINCT
    c.COMPANY_KEY,
    c.COMPANY_UID,
    c.COMPANY_KIND
FROM
    DBUSERNEB.COMPANY c
JOIN (
    -- 確認用戶同時擁有兩種身份
    SELECT 
        COMPANY_UID
    FROM 
        DBUSERNEB.COMPANY
    GROUP BY 
        COMPANY_UID
    HAVING 
        MAX(CASE WHEN COMPANY_KIND IN (1, 2) THEN 1 ELSE 0 END) = 1
        AND MAX(CASE WHEN COMPANY_KIND IN (3, 4) THEN 1 ELSE 0 END) = 1
) dual_users ON c.COMPANY_UID = dual_users.COMPANY_UID
WHERE 
    c.COMPANY_KIND IN (3, 4)
    -- 確認用戶近六個月未通過 Fubon+ 登入
    AND NOT EXISTS (
        SELECT 1
        FROM DBUSERNEB.EB_LOGIN_LOG_B l
        WHERE l.COMPANY_KEY = c.COMPANY_KEY
        AND l.CHANNEL_ID = 'M'  -- Fubon+ 通路
        AND l.LOGIN_TIME >= ADD_MONTHS(SYSDATE, -6)
    )
ORDER BY
    c.COMPANY_UID, c.COMPANY_KIND;




-- 近六個月未登入Fubon+ & 為信用卡會員客戶ID(不重複)-->
SELECT DISTINCT
    c.COMPANY_KEY,
    c.COMPANY_UID,
    c.COMPANY_KIND
FROM
    DBUSERNEB.COMPANY c
JOIN (
    -- 確認用戶同時擁有兩種身份
    SELECT 
        COMPANY_UID
    FROM 
        DBUSERNEB.COMPANY
    GROUP BY 
        COMPANY_UID
    HAVING 
        MAX(CASE WHEN COMPANY_KIND IN (1, 2) THEN 1 ELSE 0 END) = 1
        AND MAX(CASE WHEN COMPANY_KIND IN (3, 4) THEN 1 ELSE 0 END) = 1
) dual_users ON c.COMPANY_UID = dual_users.COMPANY_UID
WHERE 
    c.COMPANY_KIND IN (3, 4)
    -- 確認用戶近六個月未通過 Fubon+ 登入
    AND NOT EXISTS (
        SELECT 1
        FROM DBUSERNEB.EB_LOGIN_LOG_B l
        WHERE l.COMPANY_KEY = c.COMPANY_KEY
        AND l.CHANNEL_ID = 'M'  -- Fubon+ 通路
        AND l.LOGIN_TIME >= ADD_MONTHS(SYSDATE, -6)
    )
ORDER BY
    c.COMPANY_UID, c.COMPANY_KIND;

create or replace FUNCTION           get_dec_val( 
    p_in_hex  IN VARCHAR2, 
    p_key_hex IN VARCHAR2 ) 
  RETURN VARCHAR2 
IS 
  l_ret VARCHAR2 (2000); 
  l_dec_val raw (2000); 
  p_in raw(2000); 
  p_key raw(2000); 
  l_mod NUMBER := dbms_crypto.ENCRYPT_AES128 + dbms_crypto.CHAIN_ECB + dbms_crypto.PAD_PKCS5; 
BEGIN 
  p_in      := hextoraw(p_in_hex); 
  p_key     := hextoraw(p_key_hex); 
  l_dec_val := dbms_crypto.decrypt ( p_in, l_mod, p_key ); 
  l_ret     := UTL_I18N.RAW_TO_CHAR(l_dec_val, 'AL32UTF8'); 
  RETURN l_ret;

--20200810 peng 新增exception
EXCEPTION
   WHEN INVALID_NUMBER THEN
      dbms_output.put_line('INVALID_NUMBER');
       RETURN p_in_hex;
   WHEN NO_DATA_FOUND THEN
      dbms_output.put_line('NO_DATA_FOUND!');
       RETURN p_in_hex;
   WHEN VALUE_ERROR THEN
      dbms_output.put_line('VALUE_ERROR!');
      IF instr(p_in_hex, '@') > 0 THEN
        RETURN get_dec_val(replace(p_in_hex, '@'), p_key_hex);
      ELSE
        RETURN p_in_hex;
      END IF;
   WHEN others THEN
      dbms_output.put_line('Error!');
       RETURN p_in_hex;
END;
