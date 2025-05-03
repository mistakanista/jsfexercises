INSERT INTO 
TBC_TXN (
	TXN_TIMESTAMP
   ,TXN_MSG_ID      
   ,TXN_INDC_CRE_DB 
   ,TXN_AMT         
   ,TXN_SUBJECT     
   ,DEBTOR_IBAN     
   ,DEBTOR_NAME     
   ,DEBTOR_ADDRESS  
   ,CREDITOR_IBAN   
   ,CREDITOR_NAME   
   ,CREDITOR_ADDRESS
)
VALUES(
	current timestamp
   ,:TXN-MSG-ID      
   ,:TXN-INDC-CRE-DB 
   ,:TXN-AMT         
   ,:TXN-SUBJECT     
   ,:DEBTOR-IBAN     
   ,:DEBTOR-NAME     
   ,:DEBTOR-ADDRESS  
   ,:CREDITOR-IBAN   
   ,:CREDITOR-NAME   
   ,:CREDITOR-ADDRESS
)
