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
    /// Логика взаимодействия для AddEditPage.xaml
    /// </summary>
    public partial class AddEditPage : Page
    {
        public AddEditPage(Dish dish = null)
        {
            InitializeComponent();
        }
        private void AcceptButton_Click(object sender, RoutedEventArgs e)
        {

        }

        private void DeclineButton_Click(object sender, RoutedEventArgs e)
        {

        }
    }
}
