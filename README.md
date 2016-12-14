# TripInfo
A simple command-line application showing real-time data for selected public transportation in Switzerland.
This is a quick and dirty implementation to test the APIs of the new open data platform for Swiss public transportation.
This is to say that there's not much error handling going on. If you want to crash it, you can easily do so.

The data is provided by the [Open Data Platform Swiss Public Transport](https://opentransportdata.swiss/en/).

## Usage
1. Compile the source into a *.jar file. This is a console application that requires user input, so you most likely won't be able to execute it inside your IDE.
2. TripInfo requires two additional files in the same directory. One is the station list [bahnhof.csv](https://opentransportdata.swiss/en/dataset/bhlist/resource_permalink/bahnhof.csv), a CSV file containing all the stations. The other is a simple textfile `apikey.txt` containing your API key for the platform. You can obtain it by [registering](https://opentransportdata.swiss/en/register/) on the platform. Make sure you have these two inside the same directory as the *.jar.
3. Run the application with `java -jar TripInfo.jar`. The rest should be self-explanatory.

## License
[MIT license](LICENSE)
