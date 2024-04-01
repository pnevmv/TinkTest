# Trading Bot using Tinkoff Invest API

This trading bot implements trading with financial instruments (currently funds and stocks), making decisions to buy/sell based on the Relative Strength Index (<a href="https://en.wikipedia.org/wiki/Relative_strength_index">RSI</a>). It allows adding/removing instruments for trading and setting limits on the available funds for trading with a particular instrument. Bot management is done through command-line input. Details about available commands are provided below.
## Project Structure

The trading bot is implemented as an interactive console application, with the potential for quick feature expansion through database integration and a graphical user interface. The application is logically divided into four parts:

1. User Interaction
        Obtaining commands, handling requests, and exceptional situations.
2. Computational
        Calculating coefficients (RSI, NVI, PVI; currently only RSI is implemented), making decisions on buying and selling based on these indices.
3. Connector
        Creating threads and unary requests to the API for receiving and sending data, based on decisions made and calculated indices.
4. Data Storage
        Storing the history of all transactions, companies with available stocks for trading, and indices.

## Installation and Running

Install Java 11 or higher.
Open the command prompt (in Windows, run as administrator) and navigate to the directory with the JAR file.
Enter java -jar FileName.jar.

## Getting Started

Upon starting the bot, you'll be prompted to enter a token. You can generate a token on the Tinkoff Investments website under the token section. Then, select the account number for trading. After that, the interactive mode starts with the ability to input commands. Begin by entering the help command to familiarize yourself with all the bot's capabilities.
## Available Commands
<ul>
<li>[help]: List all commands.
<li>add: Add an instrument. Enter the FIGI of the instrument, the money provided to the bot for trading with the instrument, the maximum drawdown percentage, after which a sale occurs, and the profit percentage, after which the bot starts selling the instrument.
<li>changeCompany: Change parameters for trading (stop loss, take-profit, free money, etc.).
<li>startTrade: Start trading with the instrument. The add command only adds the instrument to the program; use this command for trading.
<li>stopTrade: Stop trading with the instrument.
<li>delete: Delete the instrument from the program.
<li>printSchedule: Print the stock exchange schedule.
<li>exit: Exit the program. All trading stops. At this stage, the program won't remember trades from the previous session upon the next launch.
