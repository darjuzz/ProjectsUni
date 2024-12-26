module Klima where

-- Diese Zeile darf nicht verändert werden
-- Sie verhindert den Einsatz konkreter Funktionen,
-- die ihr direkt oder indirekt selbst implementieren sollt
import Prelude
import Text.Read(readMaybe)
import Data.Maybe(mapMaybe)
import Klimadaten

-- Speichert eine Wettermessung an einem Ort im Format: 
-- Klimawert Land Stadt Monat Temperatur Niederschlag
data Klimawert = Klimawert { 
    land :: String, 
    stadt :: String, 
    monat :: Monat, 
    temperatur :: Double, 
    niederschlag :: Double
  }
  deriving (Eq, Show)

data Monat = Jan | Feb | Mar | Apr | Mai | Jun | Jul | Aug | Sep | Okt | Nov | Dez
  deriving (Read, Eq, Show, Enum)

monate :: [Monat]
monate = [Jan .. Dez]

-----------------

--Liest Klimadaten aus einer Zeichenkette ein und gibt eine Liste von 'Klimawert' zurück.
-- Ungültige Zeilen werden übersprungen.
einlesen :: String -> [Klimawert]
einlesen daten = mapMaybe parseLine (lines daten)
  where
    parseLine :: String -> Maybe Klimawert
    parseLine line = case words line of
      [land, stadt, monatStr, tempStr, rainStr] -> do
        monat <- readMaybe monatStr :: Maybe Monat
        temperatur <- readMaybe tempStr :: Maybe Double
        niederschlag <- readMaybe rainStr :: Maybe Double
        return $ Klimawert land stadt monat temperatur niederschlag
      _ -> Nothing


-- Gibt die geringste Temperatur für eine Stadt oder ein Land zurück
-- Liefert 'Nothing', wenn keine Daten vorhanden sind.
minTemp :: String -> [Klimawert] -> Maybe Double
minTemp ort daten =
  let relevantTemps = [temperatur k | k <- daten, stadt k == ort || land k == ort]
  in if null relevantTemps then Nothing else Just (minimum relevantTemps)

-- Gibt den höchsten Niederschlag für eine Stadt oder ein Land zurück.
-- Liefert 'Nothing', wenn keine Daten vorhanden sind.
maxNieder :: String -> [Klimawert] -> Maybe Double
maxNieder ort daten =
  let relevantRain = [niederschlag k | k <- daten, stadt k == ort || land k == ort]
  in if null relevantRain then Nothing else Just (maximum relevantRain)

-- Berechnet den Durchschnitt von Temperatur und Niederschlag für eine Stadt
-- und einen Monat. Gibt (0, 0) zurück, wenn keine Daten vorhanden sind.
durschnittKlima :: String -> Monat -> [Klimawert] -> (Int, Int)
durschnittKlima stadtName mon daten =
  let relevant = [k | k <- daten, stadt k == stadtName && monat k == mon]
      temps = [temperatur k | k <- relevant]
      rains = [niederschlag k | k <- relevant]
      avg xs = if null xs then 0 else round (sum xs / fromIntegral (length xs))
  in (avg temps, avg rains)





-----------

-- Speichert Klimainformationen zu einem Ort im Format: 
-- Stadtklima Stadt [(Monat, Temperatur, Niederschlag)]
data Stadtklima = Stadtklima { 
    ort :: String, 
    daten :: [(Monat, Int, Int)]
  }
  deriving (Show, Eq)


-- Erstellt ein 'Stadtklima' mit durchschnittlichen Werten für jeden Monat.
klima :: String -> [Klimawert] -> Stadtklima
klima stadt daten =
  Stadtklima stadt [(mon, temp, rain) | mon <- monate, let (temp, rain) = durschnittKlima stadt mon daten]

--Gibt die Monate der Vegetationsperiode zurück (mind. 5°C und
-- Niederschlag ≥ 2x Temperatur).
vegetation :: Stadtklima -> [Monat]
vegetation (Stadtklima _ daten) =
  [mon | (mon, temp, rain) <- daten, temp >= 5, rain >= 2 * temp]


