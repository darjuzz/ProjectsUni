module Main where

import Test.Tasty
import Test.Tasty.HUnit

import Klima
import Klimadaten

main = defaultMain $ testGroup "5. Übungsblatt" [exercise1, exercise2, exercise3, exercise4] 

exercise1 = testGroup "einlesen" $ [
  testCase "einlesen Stockholm" $
    einlesen "Schweden Stockholm Feb -3.00 27.00\nSchweden Stockholm Mar 0.00 26.00" @?= 
      [Klimawert "Schweden" "Stockholm" Feb (-3.00) (27.00), Klimawert "Schweden" "Stockholm" Mar (0.00) (26.00)],
  testCase "einlesen (Fehler)" $ einlesen "Schweden Stockholm May -2 -13\nFoo Baz 0" @?= [],
  testCase "einlesen klimadaten" $
    (einlesen klimadaten !! 2) @?= Klimawert "Deutschland" "Bremen" Jan 1.02 60.72
  ]

exercise2 = testGroup "Daten finden" $ [
  testCase "minTemp Bremen" $
    minTemp "Bremen" (einlesen klimadaten) @?= Just 0.91,
  testCase "minTemp Australien" $ 
    minTemp "Australien" (einlesen klimadaten) @?= Just 12.05,
  testCase "maxNieder Kairo" $ 
    maxNieder "Kairo" (einlesen klimadaten) @?= Just 6.58,
  testCase "maxNieder Kanada" $ 
    maxNieder "Kanada" (einlesen klimadaten) @?= Just 154.46,
  testCase "minTemp Stockholm" $ 
    minTemp "Stockholm" (einlesen klimadaten) @?= Nothing,
  testCase "maxNieder Schweden" $ 
    maxNieder "Schweden" (einlesen klimadaten) @?= Nothing
  ]

exercise3 = testGroup "Daten verarbeiten" $ [
  testCase "durschnittKlima Bremen Februar" $
    durschnittKlima "Bremen" Feb (einlesen klimadaten) @?= (2, 40), 
  testCase "durschnittKlima Sydney Januar" $
    durschnittKlima "Sydney" Jan (einlesen klimadaten) @?= (22, 129), 
  testCase "durschnittKlima Stockholm August" $
    durschnittKlima "Stockholm" Jan (einlesen klimadaten) @?= (0, 0) 
  ]

exercise4 = testGroup "Daten zusammenfassen" $ [
  testCase "klima Bremen" $
    klima "Bremen" (einlesen klimadaten) @?= 
      Stadtklima "Bremen" [(Jan,1,56),(Feb,2,40),(Mar,4,49),(Apr,8,47),(Mai,13,64),(Jun,17,69),(Jul,17,69),(Aug,16,68),(Sep,14,60),(Okt,10,56),(Nov,5,60),(Dez,2,60)],
  testCase "vegetation Bremen" $
    vegetation (klima "Bremen" (einlesen klimadaten)) @?= [Apr,Mai,Jun,Jul,Aug,Sep,Okt,Nov]
  ]
