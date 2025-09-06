# IBAN Checker

Dieses Projekt bietet eine Java-Implementierung zur Validierung von IBAN-Nummern gemäß dem internationalen Standard. Die Klasse `IbanChecker` prüft, ob eine eingegebene IBAN formal korrekt ist und ob die Prüfziffer stimmt. Die Validierung erfolgt durch Umwandlung des Ländercodes in numerische Werte und anschließender Modulo-97-Berechnung. Das Ergebnis der Prüfung wird als Objekt mit Status und Begründung zurückgegeben.

## Installation

1. Java 11 oder höher installieren.
2. Projekt mit Maven bauen:  
   `mvn clean install`

## Nutzung

Beispielaufruf in der `main`-Methode:
```java

Derby DB starten auf MacOS nach Installation:

$DERBY_HOME/bin/startNetworkServer

Liberty Server starten:
