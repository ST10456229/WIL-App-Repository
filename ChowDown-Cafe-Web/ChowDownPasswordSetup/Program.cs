
using Microsoft.AspNetCore.Identity;
using System.Text;

const string username = "manager@chowdown.co.za";

Console.WriteLine("CHOWDOWN CAFE - STAFF PASSWORD SETUP");
Console.WriteLine("------------------------------------");
Console.WriteLine("Create a strong password for the staff account.");
Console.Write("Enter your new password: ");

var password = ReadHiddenPassword();

if (password.Length < 12)
{
    Console.WriteLine("\nPassword must contain at least 12 characters.");
    return;
}

Console.Write("\nConfirm your password: ");
var confirmation = ReadHiddenPassword();

if (password != confirmation)
{
    Console.WriteLine("\nPasswords do not match. Run the program again.");
    return;
}

var hasher = new PasswordHasher<string>();
var hash = hasher.HashPassword(username, password);

Console.WriteLine("\n\nPASSWORD HASH GENERATED");
Console.WriteLine("Username: " + username);
Console.WriteLine("Copy this hash into User Secrets:");
Console.WriteLine(hash);
Console.WriteLine("\nKeep your password and hash private.");

static string ReadHiddenPassword()
{
    var password = new StringBuilder();

    while (true)
    {
        var key = Console.ReadKey(intercept: true);

        if (key.Key == ConsoleKey.Enter)
            break;

        if (key.Key == ConsoleKey.Backspace)
        {
            if (password.Length > 0)
                password.Length--;

            continue;
        }

        if (!char.IsControl(key.KeyChar))
            password.Append(key.KeyChar);
    }

    return password.ToString();
}
