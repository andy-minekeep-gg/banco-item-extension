<div align="center">
  <p>
    <h1>banco Item Extension</h1>
  </p>
</div>

<div id="information"></div>

## 📚 Information

> This is a banco extension based off of the banco template project.

Here's what's added
- The `/banco_item` suite of commands
- `/banco_item set <id> <worth> (<item in command form, if console>)` to add and register a new item
- `/banco_item remove <id>` to remove and unregister an existing item
- Items are saved in `items.yml` in readable format in the plugin's data folder
- `/banco_item reload` to reload the plugin's items, re-registering them

> [!WARNING]
> Please please please please do `/banco_item reload` after you do `/banco reload`, as item registered by this extension may be wiped by the reload.
