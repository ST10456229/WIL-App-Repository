
using Microsoft.AspNetCore.Authentication;
using Microsoft.AspNetCore.Authentication.Cookies;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Mvc;
using System.Security.Claims;

namespace ChowDownCafe.Controllers
{
    public class AccountController : Controller
    {
        private readonly IConfiguration _configuration;

        private readonly PasswordHasher<string> _passwordHasher =
            new PasswordHasher<string>();

        public AccountController(IConfiguration configuration)
        {
            _configuration = configuration;
        }

        [AllowAnonymous]
        [HttpGet]
        public IActionResult Login(string? returnUrl = null)
        {
            ViewBag.ReturnUrl = returnUrl;
            return View();
        }

        [AllowAnonymous]
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> Login(
            string username,
            string password,
            string? returnUrl = null)
        {
            ViewBag.ReturnUrl = returnUrl;

            if (string.IsNullOrWhiteSpace(username) ||
                string.IsNullOrWhiteSpace(password))
            {
                ViewBag.Error = "Enter your username and password.";
                return View();
            }

            var staffUsername =
                _configuration["StaffLogin:Username"];

            var staffPasswordHash =
                _configuration["StaffLogin:PasswordHash"];

            if (string.IsNullOrWhiteSpace(staffUsername) ||
                string.IsNullOrWhiteSpace(staffPasswordHash))
            {
                ViewBag.Error =
                    "Staff login has not been configured yet.";
                return View();
            }

            var verification = _passwordHasher.VerifyHashedPassword(
                staffUsername,
                staffPasswordHash,
                password);

            if (!string.Equals(
                    username.Trim(),
                    staffUsername,
                    StringComparison.OrdinalIgnoreCase) ||
                verification == PasswordVerificationResult.Failed)
            {
                ViewBag.Error = "Invalid username or password.";
                return View();
            }

            var claims = new List<Claim>
            {
                new Claim(ClaimTypes.Name, staffUsername),
                new Claim(ClaimTypes.Role, "Staff")
            };

            var identity = new ClaimsIdentity(
                claims,
                CookieAuthenticationDefaults.AuthenticationScheme);

            var principal = new ClaimsPrincipal(identity);

            await HttpContext.SignInAsync(
                CookieAuthenticationDefaults.AuthenticationScheme,
                principal);

            if (!string.IsNullOrWhiteSpace(returnUrl) &&
                Url.IsLocalUrl(returnUrl))
            {
                return Redirect(returnUrl);
            }

            return RedirectToAction("Index", "Dashboard");
        }

        [Authorize]
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> Logout()
        {
            await HttpContext.SignOutAsync(
                CookieAuthenticationDefaults.AuthenticationScheme);

            return RedirectToAction(nameof(Login));
        }

        [AllowAnonymous]
        [HttpGet]
        public IActionResult AccessDenied()
        {
            return View();
        }
    }
}
