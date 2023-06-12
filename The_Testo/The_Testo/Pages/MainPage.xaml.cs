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
    /// Логика взаимодействия для MainPage.xaml
    /// </summary>
    public partial class MainPage : Page
    {

        public MainPage()
        {
            InitializeComponent();
        }
        private void Navigate(object root, object sender)
        {
            ContentFrame.Navigate(root);
            ((TextBlock)sender).Foreground = new SolidColorBrush(Color.FromRgb(0, 0, 0));
            GlobalInfo.SelectedDish = null;
        }

        private void Main_MouseUp(object sender, MouseButtonEventArgs e)
        {
            NavigationService.Navigate(new MainPage());
            ((TextBlock)sender).Foreground = new SolidColorBrush(Color.FromRgb(0, 0, 0));

        }

        private void Dishes_MouseUp(object sender, MouseButtonEventArgs e)
        {
            Navigate(new DishesPage(), sender);
        }
        private void CurrentOrders_MouseUp(object sender, MouseButtonEventArgs e)
        {
            Navigate(new CurrentOrdersPage(), sender);
        }

        private void MakeAnOrder_MouseUp(object sender, MouseButtonEventArgs e)
        {
            Navigate(new AddEditOrderPage(), sender);
        }

        private void Exit_MouseUp(object sender, MouseButtonEventArgs e)
        {
            GlobalInfo.CurrentUser = null;
            NavigationService.Navigate(new AuthPage());
            ((TextBlock)sender).Foreground = new SolidColorBrush(Color.FromRgb(0, 0, 0));
        }

        private void TextBlock_MouseDown(object sender, MouseButtonEventArgs e)
        {
            ((TextBlock)sender).Foreground = new SolidColorBrush(Color.FromRgb(130, 50, 100));
        }

        private void Statistics(object sender, MouseButtonEventArgs e)
        {
            Navigate(new AboutPage(), sender);
        }
    }
}
