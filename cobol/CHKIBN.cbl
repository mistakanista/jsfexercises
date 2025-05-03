       IDENTIFICATION DIVISION.
      * COBOL PROGRAMM DAS VON DB2 TABELLEN LIEST UND IN EIN XML
      * SCHREIBT
      * PARMETER    : N.A.
      * CONTROL PGM : BATCH
      * SUBPROGRAMM : IBAN CHECKER CHKIBN
       PROGRAM-ID. CHKIBN.
       ENVIRONMENT DIVISION.
      ******************************************************************
       INPUT-OUTPUT SECTION.
      ******************************************************************
      *
       FILE-CONTROL.
      *    SELECT TRANSACTIONS-FILE ASSIGN TO TXIN.
      *    SELECT CUSTOMER-ACCOUNTS-FILE ASSIGN TO CUSTACC.
      *    SELECT OUTPUT-FILE ASSIGN TO OUT.
      *****************************************************************
       DATA DIVISION.
      ******************************************************************
       FILE SECTION.
      *FD TRANSACTIONS-FILE.
      *01 TRANSACTIONS-RECORD.
      *    05 FILLER PIC X(40).

      *FD CUSTOMER-ACCOUNTS-FILE.
      *01 CUSTOMER-ACCOUNTS-RECORD.
      *    05 FILLER PIC X(40).

      *FD OUTPUT-FILE.
      *01 OUTPUT-RECORD.
      *    05 FILLER PIC X(40).



       WORKING-STORAGE SECTION.
      *     EXEC SQL
      *      INCLUDE CHKIBNL1
      *     END-EXEC.
        01 WS-IBAN         .
           05 WS-IBAN-CTRY-CODE                 .
             07 WS-IBAN-CTRY-CODE-1     PIC X(01).
             07 WS-IBAN-CTRY-CODE-2     PIC X(01).
           05 WS-IBAN-CHK-SUM           PIC 9(02).
           05 WS-IBAN-BLZ               PIC 9(08).
           05 WS-IBAN-KTNR              PIC 9(10).
           05 FILLER                    PIC X(08).
        01 WS-IBAN-CTRY-CODE-X           PIC X(01).
        01 WS-IBAN-CTRY-CODE-NUM-X       PIC 9(02).
        01 WS-BBAN .
           05 WS-BBAN-CTRY-CODE          PIC X(02).
           05 WS-BBAN-CHK-NUM            PIC 9(02).
           05 WS-BBAN-BLZ                PIC 9(08).
           05 WS-BBAN-KTNR               PIC 9(10).
        01 WS-CBAN .
           05 WS-CBAN-BLZ                PIC 9(08).
           05 WS-CBAN-KTNR               PIC 9(10).
           05 WS-CBAN-CTRY-CODE-NUM-1    PIC 9(02).
           05 WS-CBAN-CTRY-CODE-NUM-2    PIC 9(02).
           05 WS-CBAN-CTRY-CODE-NUM-3    PIC 9(02) VALUE  00  .
        01 WS-RETURN-CODES .
           05 WS-RETURN-GOOD             PIC 9(02) VALUE 00 .
           05 WS-RETURN-WARNING          PIC 9(02) VALUE 04 .
           05 WS-RETURN-ERROR            PIC 9(02) VALUE 08 .
           05 WS-RETURN-SEVERE           PIC 9(02) VALUE 12 .
        01  WS-LUHN-VARS .
            05  WS-DATA                 PIC X(44)
            VALUE '00000000000000000000000000000000'.
            05  WS-REDEF                REDEFINES WS-DATA.
                10  WS-A                PIC X(09).
                10  WS-B                PIC X(07).
                10  WS-C                PIC X(07).
                10  WS-D                PIC X(07).
                10  WS-E                PIC X(07).
                10  WS-F                PIC X(07).
            05  WS-E-NUM                PIC 9(14).
            05  WS-MODULUS-VALUE        PIC 9(02) VALUE 97.
            05  WS-RESULT               PIC 9(02).
            05  WS-E-DIGITS             PIC 9(02).
            05  WS-DIVISOR              PIC 9(09).
            05  WS-DIVISOR-TMP    .
              07  WS-DIVISOR-1-2        PIC X(02).
              07  WS-DIVISOR-3-9        PIC X(07).
            05  WS-DIVIDEND             PIC 9(09).
        LINKAGE SECTION.
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


        PROCEDURE DIVISION USING CHKIBN-BLOCK.
       MAIN-LOGIC.
           DISPLAY '++ CHECKIBAN ENTRY'
           DISPLAY '++ CHKIBN-BLOCK ' CHKIBN-BLOCK
           PERFORM A0001-CALC-LUHN.

           DISPLAY '++ CHECKIBAN EXIT'
           GOBACK.
      * END PROGRAM "CHKIBN".
       A0001-CALC-LUHN.
           MOVE CHKIBN-IBAN TO WS-IBAN
           DISPLAY '++ IBAN-CTRY-CODE :' WS-IBAN-CTRY-CODE
           DISPLAY '++ IBAN-CHK-SUM   :' WS-IBAN-CHK-SUM
           DISPLAY '++ IBAN-BLZ       :' WS-IBAN-BLZ
           DISPLAY '++ IBAN-KTNR      :' WS-IBAN-KTNR
      * CONVERT CTRY CODE TO NUMERIC
           MOVE  WS-IBAN-CTRY-CODE-1
             TO  WS-IBAN-CTRY-CODE-X
           PERFORM A0011-CALC-LUHN-CTRY-CODE-NUM
           MOVE  WS-IBAN-CTRY-CODE-NUM-X
             TO  WS-CBAN-CTRY-CODE-NUM-1
           MOVE  WS-IBAN-CTRY-CODE-2
             TO  WS-IBAN-CTRY-CODE-X
           PERFORM A0011-CALC-LUHN-CTRY-CODE-NUM
           MOVE  WS-IBAN-CTRY-CODE-NUM-X
             TO  WS-CBAN-CTRY-CODE-NUM-2
      * BUILD CBAN
           MOVE WS-IBAN-CTRY-CODE
             TO WS-BBAN-CTRY-CODE
           MOVE WS-IBAN-BLZ
             TO WS-BBAN-BLZ
                WS-CBAN-BLZ
           MOVE WS-IBAN-KTNR
             TO WS-BBAN-KTNR
                WS-CBAN-KTNR
           DISPLAY '++ WS-BBAN         ' WS-BBAN
           DISPLAY '++ WS-CBAN         ' WS-CBAN
           MOVE WS-CBAN TO WS-DATA
           PERFORM A0012-CALC-MODULUS .
           COMPUTE WS-BBAN-CHK-NUM = 98 - WS-RESULT
      *    DISPLAY 'WS-BBAN-CHK-NUM ' WS-BBAN-CHK-NUM
           MOVE WS-BBAN          TO CHKIBN-RETURN-IBAN
           IF WS-BBAN-CHK-NUM  = WS-IBAN-CHK-SUM
              MOVE WS-RETURN-GOOD TO CHKIBN-RETURN-CODE
           ELSE
              MOVE WS-RETURN-WARNING TO CHKIBN-RETURN-CODE
           END-IF
           .
       A0011-CALC-LUHN-CTRY-CODE-NUM .
           DISPLAY 'A0011'
           EVALUATE WS-IBAN-CTRY-CODE-X
             WHEN "A"
                 MOVE 10 TO WS-IBAN-CTRY-CODE-NUM-X
             WHEN "B"
                 MOVE 11 TO WS-IBAN-CTRY-CODE-NUM-X
             WHEN "C"
                 MOVE 12 TO WS-IBAN-CTRY-CODE-NUM-X
             WHEN "D"
                 MOVE 13 TO WS-IBAN-CTRY-CODE-NUM-X
             WHEN "E"
                 MOVE 14 TO WS-IBAN-CTRY-CODE-NUM-X
             WHEN "F"
                 MOVE 15 TO WS-IBAN-CTRY-CODE-NUM-X
             WHEN "G"
                 MOVE 16 TO WS-IBAN-CTRY-CODE-NUM-X
             WHEN "H"
                 MOVE 17 TO WS-IBAN-CTRY-CODE-NUM-X
             WHEN "I"
                 MOVE 18 TO WS-IBAN-CTRY-CODE-NUM-X
             WHEN "J"
                 MOVE 19 TO WS-IBAN-CTRY-CODE-NUM-X
             WHEN "K"
                 MOVE 20 TO WS-IBAN-CTRY-CODE-NUM-X
             WHEN "L"
                 MOVE 21 TO WS-IBAN-CTRY-CODE-NUM-X
             WHEN "M"
                 MOVE 22 TO WS-IBAN-CTRY-CODE-NUM-X
             WHEN "N"
                 MOVE 23 TO WS-IBAN-CTRY-CODE-NUM-X
             WHEN "O"
                 MOVE 24 TO WS-IBAN-CTRY-CODE-NUM-X
             WHEN "P"
                 MOVE 25 TO WS-IBAN-CTRY-CODE-NUM-X
             WHEN "Q"
                 MOVE 26 TO WS-IBAN-CTRY-CODE-NUM-X
             WHEN "R"
                 MOVE 27 TO WS-IBAN-CTRY-CODE-NUM-X
             WHEN "S"
                 MOVE 28 TO WS-IBAN-CTRY-CODE-NUM-X
             WHEN "T"
                 MOVE 29 TO WS-IBAN-CTRY-CODE-NUM-X
             WHEN "U"
                 MOVE 30 TO WS-IBAN-CTRY-CODE-NUM-X
             WHEN "V"
                 MOVE 31 TO WS-IBAN-CTRY-CODE-NUM-X
             WHEN "W"
                 MOVE 32 TO WS-IBAN-CTRY-CODE-NUM-X
             WHEN "X"
                 MOVE 33 TO WS-IBAN-CTRY-CODE-NUM-X
             WHEN "Y"
                 MOVE 34 TO WS-IBAN-CTRY-CODE-NUM-X
             WHEN "Z"
                 MOVE 35 TO WS-IBAN-CTRY-CODE-NUM-X
             WHEN OTHER
                 DISPLAY 'WIERED CHAR1' WS-IBAN-CTRY-CODE-X
           END-EVALUATE .
           DISPLAY 'LEAVE A0011'
           .
      ********************************************************
       A0012-CALC-MODULUS            .
           DISPLAY 'START A0012'
             COMPUTE WS-DIVISOR = FUNCTION NUMVAL (WS-A)
             DIVIDE WS-DIVISOR BY WS-MODULUS-VALUE
                 GIVING WS-DIVISOR
                 REMAINDER WS-RESULT
             DISPLAY 'FIRST  DIVISION ' WS-A ' ' WS-RESULT
           IF WS-B NOT = SPACE
             MOVE WS-RESULT TO WS-DIVISOR-1-2
             MOVE WS-B      TO WS-DIVISOR-3-9
             COMPUTE WS-DIVISOR = FUNCTION NUMVAL (WS-DIVISOR-TMP)
             DIVIDE WS-DIVISOR BY WS-MODULUS-VALUE
                 GIVING WS-DIVIDEND
                 REMAINDER WS-RESULT
             DISPLAY 'SECOND DIVISION ' WS-DIVISOR ' ' WS-RESULT
           END-IF
           IF WS-C NOT = SPACE
             MOVE WS-RESULT TO WS-DIVISOR-1-2
             MOVE WS-C      TO WS-DIVISOR-3-9
             COMPUTE WS-DIVISOR = FUNCTION NUMVAL (WS-DIVISOR-TMP)
             DIVIDE WS-DIVISOR BY WS-MODULUS-VALUE
                 GIVING WS-DIVIDEND
                 REMAINDER WS-RESULT
             DISPLAY 'THIRD  DIVISION ' WS-DIVISOR ' ' WS-RESULT
           END-IF
           IF WS-D NOT = SPACES
             MOVE WS-RESULT TO WS-DIVISOR-1-2
             MOVE WS-D      TO WS-DIVISOR-3-9
             COMPUTE WS-DIVISOR = FUNCTION NUMVAL (WS-DIVISOR-TMP)
             DIVIDE WS-DIVISOR BY WS-MODULUS-VALUE
                 GIVING WS-DIVIDEND
                 REMAINDER WS-RESULT
             DISPLAY 'FOURTH DIVISION ' WS-DIVISOR ' ' WS-RESULT
           END-IF
           IF WS-E NOT = SPACES
             MOVE WS-RESULT TO WS-DIVISOR-1-2
             MOVE WS-E      TO WS-DIVISOR-3-9
             COMPUTE WS-DIVISOR = FUNCTION NUMVAL (WS-DIVISOR-TMP)
             MOVE WS-DIVISOR-TMP TO WS-DIVISOR
             DIVIDE WS-DIVISOR BY WS-MODULUS-VALUE
                    GIVING WS-DIVIDEND
                    REMAINDER WS-RESULT
             DISPLAY 'FIFTH DIVISION  ' WS-DIVISOR ' ' WS-RESULT
           END-IF
           DISPLAY 'EXIT  A0012'
           .
