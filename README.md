Temat projektu:
Tematem projektu jest stworzenie systemu gry w warcaby, który umożliwia rozgrywkę
między dwoma graczami. System składa się z aplikacji klienta oraz serwera, które
komunikują się ze sobą w celu synchronizacji stanu gry. Aplikacja klienta jest napisana w
języku Java z wykorzystaniem biblioteki JavaFX do tworzenia interfejsu użytkownika, serwer
zarządza logiką gry i komunikacją między graczami.
Założenia dodatkowe:
1. Interfejs użytkownika – Gra została zaprojektowana z prostym, ale funkcjonalnym
interfejsem, wykorzystującym graficzne przedstawienie planszy warcabowej (8x8)
oraz pionków w dwóch wersjach. Pionki mogą być normalne lub promowane na
króla, co wpływa na zasady ich ruchów.
2. Obsługa ruchów – Gra umożliwia graczom wykonywanie ruchów zarówno prostych,
jak i skoków (bicia przeciwnych pionków). Pionki poruszają się po planszy na
zasadzie skoków, a po osiągnięciu ostatniego rzędu planszy, pionek może zostać
promowany na króla, co daje mu dodatkowe ruchy w tym poruszanie się i bicie do
tyłu.
3. Sprawdzanie warunków zwycięstwa – Gra automatycznie sprawdza, czy któryś z
graczy wygrał, eliminując wszystkie pionki przeciwnika.
4. Zapisy gry - System obsługuje zapis i odczyt stanu gry.
5. Przesył danych - System wykorzystuje JSON do przesyłania danych między
klientem a serwerem.
6. Logi - Logi są zarządzane za pomocą biblioteki log4j.
7. Aplikacja - Uruchamiana jest przy pomocy pliku Game.bat który jest umiejscowiony
w lokalizacji (\out\artifacts\Server). Po uruchomieniu aktywuje serwer oraz 2 klienty
Opis architektury systemu:
1. Serwer (Serwer.java, ClientHandler.java, ServerListener.java,
ServerLogger.java):
● Nasłuchuje połączeń od klientów na określonym porcie.
● Zarządza logiką gry, w tym ruchem pionków, sprawdzeniem warunków
zwycięstwa oraz synchronizacją stanu gry.
● Obsługuje zapisywanie i ładowanie stanów gry.
2. Klient (Client.java, ClientUI.java)
● Uruchamia aplikację klienta i łączy się z serwerem.
● Inicjalizuje interfejs użytkownika za pomocą JavaFX
● Zarządza połączeniem z serwerem oraz komunikacją.
3. Interfejs użytkownika (MainMenu.java, CheckersGame.java):
● MainMenu.java: Tworzy główne menu gry z opcjami do rozpoczęcia nowej
gry, załadowania gry, wyświetlenia twórców oraz wyjścia.
● CheckersGame.java: Zarządza logiką gry, w tym ruchem pionków,
aktualizacją stanu planszy oraz wyświetlaniem wyników.
4. Model danych (Piece.java, PieceType.java, Tile.java, GameState.java,
PieceState.java):
● Piece.java: Reprezentuje pionek na planszy.
● PieceType.java: Enum definiujący typy pionków (DOG, CAT).
● Tile.java: Reprezentuje pojedyncze pole na planszy.
● GameState.java: Reprezentuje stan gry, w tym obecnego gracza i listę
pionków.
● PieceState.java: Reprezentuje stan pojedynczego pionka.
5. Technologie:
● Java – Język programowania, w którym została napisana aplikacja.
● JavaFX – Biblioteka do tworzenia interfejsów graficznych. Używana do wyświetlania
planszy, pionków i zarządzania interakcją z użytkownikiem.
● Jar (Java Archive) - Jest to standardowy format pakietu Javy, który pozwala na
przechowywanie plików klas, bibliotek i zasobów w jednym pliku archiwum.
● Maven - Narzędzie do zarządzania zależnościami oraz pobierania bibliotek z
repozytorium
● log4j - biblioteka języka programowania Java służąca do tworzenia logów podczas
działania aplikacji.
● Mockito - biblioteka która jest niezbędnym narzędziem do tworzenia testów
jednostkowych. Pozwala na pozbycie się niechcianej zależności, żeby łatwiej móc
testować w izolacji.
● JUnit - biblioteka wspomagająca automatyzację testów w języku Java. Pozwala na
przeprowadzenie testów jednostkowych wybranej klasy
● JSON (JavaScript Object Notation) - otwarty format zapisu struktur danych.
Pozwala na skuteczną wymianę danych pomiędzy aplikacjami
6. Przepływ danych
● Gracz uruchamia aplikację kliencką i wybiera rolę (DOG lub CAT).
● Aplikacja kliencka łączy się z serwerem i wysyła informacje o roli gracza.
● Serwer zarządza stanem gry i synchronizuje go między klientami.
● Gracze wykonują ruchy, które są przesyłane do serwera, gdzie są
weryfikowane i synchronizowane.
● Serwer sprawdza warunki zwycięstwa i informuje klientów o wyniku gry.
● Gracze mogą zapisać stan gry, który jest przechowywany na serwerze i może
być później załadowany.
1. Testy jednostkowe Testy jednostkowe zostały zaimplementowane przy użyciu
biblioteki JUnit 5 oraz Mockito do mockowania zależności. Testy obejmują
następujące klasy:
● CheckersGameTest.java: Testuje logikę gry, w tym ruchy pionków,
aktualizację tury, sprawdzanie warunków zwycięstwa oraz zapis stanu gry.
● ClientHandlerTest.java: Testuje obsługę komunikacji z klientem, w tym
przetwarzanie ruchów, zapisywanie i ładowanie stanu gry oraz ustawianie roli.
● ClientUITest.java: Testuje połączenie z serwerem, wysyłanie ruchów,
zapisywanie stanu gry oraz rozłączanie klienta.
● MainMenuTest.java: Testuje działanie interfejsu głównego menu, w tym
przyciski do rozpoczęcia gry, załadowania gry, wyświetlenia twórców oraz
wyjścia.
● PieceTest.java: Testuje inicjalizację pionków, ustawianie pozycji oraz
promowanie pionków na króla.
● ServerTest.java: Testuje uruchamianie serwera oraz logowanie.
● TileTest.java: Testuje inicjalizację pól planszy oraz zarządzanie pionkami na
polach.
● WinLoseWindowTest.java: Testuje działanie okna wyświetlającego wynik gry.
2. Testy integracyjne Testy integracyjne zostały przeprowadzone manualnie,
sprawdzając poprawność działania komunikacji między klientem a serwerem,
synchronizację stanu gry oraz poprawność wyświetlania interfejsu użytkownika.
