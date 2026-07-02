# Blatt 07: Zoo

## Generics

Generics helfen im Zoo-Szenario dabei, falsche Typen bereits zur Compile-Zeit zu verhindern. Ein Gehege wird mit `Enclosure<T extends Animal>` definiert. Dadurch kann ein Gehege nur Tiere oder Untertypen von `Animal` enthalten.

Ein Beispiel ist `Aquarium<T extends Fish>`. In ein Aquarium koennen dadurch nur Fische oder Untertypen von `Fish` eingefuegt werden. Ein `Dog` oder ein `Lion` waere dort ein Compilerfehler. Auch `CatHouse` ist bewusst auf `Lion` festgelegt, sodass dort kein `Tiger` eingefuegt werden kann.

## Logging

Systematisches Logging ist sinnvoller als einfache Ausgaben mit `System.out.println`, weil Log-Meldungen nach Wichtigkeit gefiltert werden koennen. Mit Log-Leveln wie `INFO`, `FINE`, `WARNING` und `SEVERE` kann man besser steuern, welche Informationen angezeigt werden.

`INFO` nutze ich fuer normale Methodenaufrufe.  
`FINE` nutze ich fuer genauere Zustandsinformationen nach einer erfolgreichen Ausfuehrung.  
`WARNING` nutze ich, wenn ein gesuchtes Gehege nicht gefunden wird.  
`SEVERE` nutze ich bei schwerwiegenden Fehlern, zum Beispiel bei ungueltigen Parametern.

## Streams

Streams waren besonders hilfreich beim Sammeln, Filtern und Zaehlen der Tiere. Zum Beispiel koennen mit `flatMap` alle Tiere aus allen Gehegen in eine Liste gebracht werden. Mit `filter` werden bestimmte Tiere gesucht, und mit `Collectors.groupingBy` werden Tiere nach Typ gezaehlt.

Etwas unuebersichtlich werden Streams, wenn mehrere Operationen direkt hintereinander verschachtelt werden. Fuer einfache Abfragen sind sie aber kuerzer und lesbarer als klassische Schleifen.