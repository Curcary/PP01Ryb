using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;
using The_Testo.Database;

namespace The_Testo.Pages
{
    /// <summary>
    /// Логика взаимодействия для AuthPage.xaml
    /// </summary>
    public partial class AuthPage : Page
    {

        public AuthPage()
        {
            InitializeComponent();
#if DEBUG
            EmailBox.Text = "login@log.com";
            PassBox.Password = "12345";
#endif

        }

        private void Button_Click(object sender, RoutedEventArgs e)
        {
            
            User user = DB.entities.User.FirstOrDefault
                (s => s.UserEmail == EmailBox.Text && s.UserPassword == PassBox.Password);
            if (user != null)
            {
                if (user.RoleID == 1)
                {
                    GlobalInfo.CurrentUser = user;
                    NavigationService.Navigate(new MainPage());

                }
                else MessageBox.Show("В доступе отказано");
            }
            else MessageBox.Show("Неправильный логин или пароль");
        }
    }
}
