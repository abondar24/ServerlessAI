#!/bin/bash

node split-training-test.js ./reviews_Automotive_5.json ./automotive.json
node split-training-test.js ./reviews_Office_Products_5.json ./office.json
node split-training-test.js ./reviews_Beauty_5.json ./beauty.json
node split-training-test.js ./reviews_Pet_Supplies_5.json ./pet.json

node build-training-set.js ./automotive.json AUTO
node build-training-set.js ./office.json OFFICE
node build-training-set.js ./beauty.json BEAUTY
node build-training-set.js ./pet.json PET

cat ./AUTO ./OFFICE ./BEAUTY ./PET > ./data.csv

