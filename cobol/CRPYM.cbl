       PROCESS NODYNAM,RENT,APOST,CICS,TRUNC(OPT)

      *****************************************************************
      *
      *****************************************************************

       IDENTIFICATION DIVISION.
       PROGRAM-ID.              crpym   .
       DATE-WRITTEN.            Febr 2024
      *
       ENVIRONMENT DIVISION.
      *
       DATA DIVISION.
      *
       WORKING-STORAGE SECTION.
           EXEC SQL
            INCLUDE SQLCA
           END-EXEC.

      01 CHKIBN-BLOCK.
        03 LT-CHKIBN               PIC X(8) VALUE "CHKIBN  ".
        03 CHKIBN-PARMS.
        05 CHKIBN-IBAN           PIC X(30).
        03 CHKIBN-FLAGS.
          05 CHKIBN-FLAG-CHECK   PIC X(1) VALUE "Y".
          05 CHKIBN-FLAG-BUILD   PIC X(1) VALUE "Y".
        03 CHKIBN-RETRUN-BLOCK .
          05 CHKIBN-RETURN-CODE  PIC 9(02).
          05 CHKIBN-REASON-CODE  PIC X(08).
          05 CHKIBN-RETURN-IBAN  PIC X(30).

      01 GRP-TXN-DATA   .
        03  TXN-MSG-ID      PIC X(12)  .
        03  TXN-INDC-CRE-DB  PIC X(01)   .
          88 TXN-INDC-CRE    VALUE "C"   .
          88 TXN-INDC-DB     VALUE "D"   .
        03  TXN-AMT          PIC 9(12)   .
        03  TXN-SUBJECT      PIC X(144)  .
        03  DEBTOR-IBAN      PIC X(35)   .
        03  DEBTOR-NAME      PIC X(50)   .
        03  DEBTOR-ADDRESS   PIC X(50)   .
        03  CREDITOR-IBAN    PIC X(35)   .
        03  CREDITOR-NAME    PIC X(50)   .
        03  CREDITOR-ADDRESS PIC X(50)   .
        03  TXN-INDC-END     PIC X(01)  VALUE "§" .
        03  FILLER-IBAN      PIC X(160) VALUE SPACES .

      *
      *    Working storage definitions
         01 ws-vars .
            02 WS-TXN-LEN           pic 9(6) value zero .
         01 ws-err-codes            pic 9(4) .
            88  ws-err-noerror      value '0000' .
            88  ws-err-inv-len      value '0030' .
            88  ws-err-inv-deb-iban value '0040' .
            88  ws-err-inv-cre-iban value '0050' .
            88  ws-err-sql-ins-fail value '0060' .
      *
       PROCEDURE DIVISION.
      *
       MAIN-PROCESSING SECTION.
           DISPLAY "cre paymnent  start"
           set  ws-err-noerror      to true
           PERFORM A0001-retr-msg-data  .
           PERFORM A0002-chk-msg-data   .
           if ws-err-noerror
             PERFORM A0003-format-data    .
             PERFORM A0004-write-data     .
           end-if
           DISPLAY "cre paymnent  ende "
           STOP RUN.
           GOBACK.
      *
       A0001-retrive-msg-data    .
           DISPLAY "A0001 retieve data start "
           EXEC CICS RETRIEVE
                     INTO(GRP-TXN-DATA)
                     LENGTH(WS-TXN-LEN)
           end-exec .
           DISPLAY "A0001 retieve data end   "
           continue
           .
      *------------------------------
       A0002-chk-msg-data   .
           DISPLAY "A0002 check msg data start"
           if ws-txn-len  < 440
             set ws-err-inv-len to true
           end-if
           MOVE DEBTOR-IBAN    TO CHKIBN-IBAN
           perform a0005-call-check-iban
           if CHKIBN-RETURN-CODE > 4
              set ws-err-inv-deb-iban to true
           end-if
           MOVE CREDIDTOR-IBAN TO CHKIBN-IBAN
           perform a0005-call-check-iban
           if CHKIBN-RETURN-CODE > 4
              set ws-err-inv-cre-iban to true
           end-if
           move
           DISPLAY "A0002 check msg data end  "
           continue
           .
       A0003-format-data    .
           DISPLAY "A0003 format data start "
           DISPLAY "A0003 format data end   "
       A0004-write-data     .
           DISPLAY "A0003 write data start "
           exec sql
             include CRPYMS1
           end-exec .
           if sq-code > 4
              ws-err-sql-ins-fail to true
           end
           DISPLAY "A0003 write data end   "
       A0005-CALL-CHCK-IBAN .
           DISPLAY "A0005 CALL CHECK IBAN START"
            CALL LT-CHKIBN USING CHKIBN-BLOCK
            END-CALL .
           DISPLAY  LT-CHKIBN ' RC ' RETURN-CODE
           DISPLAY "A0005 CALL CHECK IBAN EXIT"
           .