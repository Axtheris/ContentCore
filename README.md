# ContentCore

ContentCore is a powerful Minecraft plugin that introduces an item-based economy system using diamonds as currency. It provides a range of features to enhance player interactions and economic gameplay on your server.

## Features

- **Diamond-based Economy**: Players can deposit and withdraw diamonds from their virtual bank account.
- **Player-to-Player Transactions**: Easy transfer of diamonds between players.
- **Cheque System**: Create and redeem diamond cheques for secure transactions.
- **Interest System**: Earn interest on your diamond balance over time.
- **Permission-based Commands**: Granular control over who can use specific economy features.

## Commands

All commands are accessible through the main `/cc` command:

- `/cc bank`: Check your current diamond balance.
- `/cc pay <player> <amount>`: Pay diamonds to another player.
- `/cc deposit`: Deposit diamonds from your inventory into your bank account.
- `/cc withdraw <amount>`: Withdraw diamonds from your bank account to your inventory.
- `/cc cheque <amount>`: Create a diamond cheque.
- `/cc reload`: Reload the plugin configuration (admin only).

## Installation

1. Download the latest `ContentCore.jar` file from the releases page.
2. Place the JAR file in your server's `plugins` folder.
3. Restart your server or run the `/reload` command.

## Configuration

The plugin uses default settings out of the box. If you need to adjust any settings, they can be found in the `config.yml` file in the plugin's folder after the first run.

## Permissions

- `contentcore.*`: Grants access to all ContentCore commands (except reload).
- `contentcore.reload`: Allows reloading the plugin (default: op).
- `contentcore.bank`: Allows checking bank balance (default: true).
- `contentcore.pay`: Allows paying diamonds to other players (default: true).
- `contentcore.deposit`: Allows depositing diamonds (default: true).
- `contentcore.withdraw`: Allows withdrawing diamonds (default: true).
- `contentcore.cheque`: Allows creating diamond cheques (default: true).

## Support

If you encounter any issues or have suggestions, please open an issue on our GitHub repository.

## Contributing

We welcome contributions! Please feel free to submit a Pull Request.

## License

This project is licensed under the MIT License - see the LICENSE file for details.
